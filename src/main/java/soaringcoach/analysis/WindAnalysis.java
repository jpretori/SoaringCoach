package soaringcoach.analysis;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.FlightAnalyser;
import soaringcoach.Thermal;

/**
 * Works out the wind direction and speed (in m/s) for each thermal. Done by
 * calculating the direction and distance that each circle in the thermal
 * drifted from the previous circle (the “drift vector”). Once an array of drift
 * vectors exists, iterate over it and take the average drift vector. Then
 * successively refine this by throwing out outliers (if a specific drift vector
 * differs from the average by more than, say, 10% it’s an outlier and should be
 * discarded) and recalculating the average. If this process results in an empty
 * array of drift vectors, the wind speed for the thermal can not be calculated
 * and it should be highlighted to the pilot that he needs to fly more
 * consistent circles when thermalling.
 * 
 * @author johanpretorius
 *
 */
public class WindAnalysis extends AAnalysis {
	private static final double DRIFT_OUTLIER_CUTOFF_SIZE = 20*4;
	private static final double DRIFT_OUTLIER_CUTOFF_BEARING = 5*9;
	private static final int MIN_DRIFT_VECTORS_REQUIRED = 3;

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		if (flight.is_wind_analysis_complete) {
			return flight;
		}
		
		if (flight.thermals == null) {
			throw new AnalysisException("Thermal array is null - cannot perform wind analysis until thermal analysis is completed");
		}
		
		for (Thermal t : flight.thermals) {
			t = calculateDriftVectors(t);
						
			t = refineAverageDrift(t);
		}
		
		flight.is_wind_analysis_complete = true;
		return flight;
	}

	/**
	 * Refine out a wind estimate by:<br>
	 * 1. averaging drift vectors 
	 * <br>
	 * 2. throwing out outliers <br>
	 * 3. re-calculating the average<br>
	 * 4. repeat as necessary<br>
	 * <br>
	 * if this process ends in a complete lack of drift vectors, it means the
	 * pilot flew very erratically.  In this case Thermal.wind is left null.
	 * 
	 * @param t
	 */
	private Thermal refineAverageDrift(Thermal t) {
		ArrayList<PolarVector> driftVectorsPolar = getPolarVectorsList(t);
		
		ArrayList<Vector2d> driftVectorsCartesian = getCartesianVectorsList(t);
		
		Vector2d averageDriftCartesian = null;
		PolarVector averageDriftPolar = null;
		boolean justRemovedOne = false;
		int driftVectorsCount = driftVectorsCartesian.size();
		do {
			
			averageDriftCartesian = vectorCartesianCalcAverage(driftVectorsCartesian, driftVectorsCount); 
			
			averageDriftPolar = vectorCartesianToPolar(averageDriftCartesian);
			
			//look for an outlier, discard it if found and re-calculate the average
			boolean sizeCutoff = false;
			boolean bearingCutoff = false;
			justRemovedOne = false;
			for (int i = 0; (i < driftVectorsCount) && !(justRemovedOne); i++) {
				PolarVector driftVectorPolar = driftVectorsPolar.get(i);
				
				sizeCutoff = Math.abs(driftVectorPolar.size - averageDriftPolar.size) > DRIFT_OUTLIER_CUTOFF_SIZE;
				bearingCutoff = Math.abs(
						FlightAnalyser.calcBearingChange(driftVectorPolar.bearing, averageDriftPolar.bearing)) > 
						DRIFT_OUTLIER_CUTOFF_BEARING;
						
				if (sizeCutoff || bearingCutoff) {
					driftVectorsCartesian.remove(i);
					driftVectorsPolar.remove(i);
					driftVectorsCount  = driftVectorsCartesian.size();
					System.out.println("Discarded drift vector as a wind effect because: " + (sizeCutoff ? "SIZE" : "") + ", " + (bearingCutoff ? "BEARING" : ""));
					justRemovedOne = true;
				}
			}
		} while (justRemovedOne && 
				driftVectorsCount >= MIN_DRIFT_VECTORS_REQUIRED);
		
		long averageCircleDuration = t.getAverageCircleDuration();
		t.wind = calculateAverageWind(driftVectorsCount, averageDriftPolar, averageCircleDuration);
		
		t.could_not_calculate_wind = driftVectorsCount < MIN_DRIFT_VECTORS_REQUIRED;
		
		return t;
	}

	/**
	 * @param t
	 * @return
	 */
	private ArrayList<Vector2d> getCartesianVectorsList(Thermal t) {
		ArrayList<Vector2d> driftVectorsCartesian = new ArrayList<>();
		for (Circle c : t.circles) {
			if (c.drift_vector != null) {
				Vector2d driftCartesian = vectorPolarToCartesian(c);
				
				driftVectorsCartesian.add(driftCartesian);
			}
		}
		return driftVectorsCartesian;
	}

	/**
	 * @param t
	 * @return
	 */
	private ArrayList<PolarVector> getPolarVectorsList(Thermal t) {
		ArrayList<PolarVector> driftVectorsPolar = new ArrayList<>();
		for (Circle c : t.circles) {
			if (c.drift_vector != null) {
				driftVectorsPolar.add(c.drift_vector);
			}
		}
		return driftVectorsPolar;
	}

	/**
	 * @param driftVectorsCount
	 * @param averageDriftPolar
	 * @param averageCircleDuration
	 * @return
	 */
	private PolarVector calculateAverageWind(int driftVectorsCount, PolarVector averageDriftPolar,
			long averageCircleDuration) {
		
		PolarVector wind = new PolarVector(0, 0);
		
		if (driftVectorsCount > 0) {
			//now we have a wind vector, except that it's size is in "meters per circle"
			averageDriftPolar.size = 1.0 * averageDriftPolar.size / averageCircleDuration;
			wind = averageDriftPolar;
			
			//atan2 can return negative angles.  Clamp to positive.
			if (wind.bearing < 0) {
				wind.bearing += 360;
			}
		} 

		return wind;
	}

	/**
	 * @param averageDriftCartesian
	 * @return
	 */
	private PolarVector vectorCartesianToPolar(Vector2d averageDriftCartesian) {
		PolarVector averageDriftPolar;
		double average_drift_bearing = Math.atan2(averageDriftCartesian.y, averageDriftCartesian.x);
		average_drift_bearing = Math.toDegrees(average_drift_bearing);
		
		double average_drift_size = Math.sqrt(
				averageDriftCartesian.x * averageDriftCartesian.x + 
				averageDriftCartesian.y * averageDriftCartesian.y);
		
		averageDriftPolar = new PolarVector(average_drift_bearing, average_drift_size);
		return averageDriftPolar;
	}

	/**
	 * @param driftVectorsCartesian
	 * @param driftVectorsRemainingCount
	 * @return
	 */
	private Vector2d vectorCartesianCalcAverage(ArrayList<Vector2d> driftVectorsCartesian,
			int driftVectorsRemainingCount) {
		Vector2d averageDriftCartesian;
		//Get average cartesian drift vector, and convert back to polar
		averageDriftCartesian = new Vector2d(0, 0);
		for (Vector2d drift_cartesian : driftVectorsCartesian) {
			averageDriftCartesian.add(drift_cartesian); 
		}
		averageDriftCartesian.scale(1.0 / driftVectorsRemainingCount);
		return averageDriftCartesian;
	}

	/**
	 * @param c
	 * @return
	 */
	private Vector2d vectorPolarToCartesian(Circle c) {
		double x = c.drift_vector.size * Math.cos(Math.toRadians(c.drift_vector.bearing));
		double y = c.drift_vector.size * Math.sin(Math.toRadians(c.drift_vector.bearing));
		
		Vector2d driftCartesian = new Vector2d(x, y);
		return driftCartesian;
	}

	/**
	 * Iterate over circles in the thermal, calculate drift vector from each
	 * circle to the next. Circle objects are updated in-place with drift vector
	 * information, but the Thermal is returned anyway, for good form.
	 * 
	 * @param t Thermal containing some circles
	 */
	private Thermal calculateDriftVectors(Thermal t) {
		Circle c1 = null;
		for (Circle c2 : t.circles) {
			if (c1 != null) {
				GNSSPoint c1_start = c1.getStartPoint();
				GNSSPoint c2_start = c2.getStartPoint();
				
				double drift_bearing = FlightAnalyser.calculateTrackCourse(c1_start, c2_start);
				double drift_distance = c1_start.distance(c2_start);
				
				c2.drift_vector = new PolarVector(drift_bearing, drift_distance); 			
			} else {
				c2.drift_vector = new PolarVector(0, 0);
			}
			
			c1 = c2;
		}
		
		return t;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_wind_analysis_complete;
	}

}
