package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import javax.vecmath.Vector2d;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.Thermal;

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
public class WindAnalysis implements IAnalysis {
	private static final double DRIFT_OUTLIER_CUTOFF_SIZE = 20;
	private static final double DRIFT_OUTLIER_CUTOFF_BEARING = 5;

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
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
		//initially, just populate the drift vector arrays (polar and cartesian versions) and store how many vectors we have
		ArrayList<Vector2d> drift_vectors_cartesian = new ArrayList<>();
		ArrayList<PolarVector> drift_vectors_polar = new ArrayList<>();
		for (Circle c : t.getCircles()) {
			if (c.drift_vector != null) {
				drift_vectors_polar.add(c.drift_vector);
				
				double x = c.drift_vector.size * Math.cos(Math.toRadians(c.drift_vector.bearing));
				double y = c.drift_vector.size * Math.sin(Math.toRadians(c.drift_vector.bearing));
				
				Vector2d drift_cartesian = new Vector2d(x, y);
				drift_vectors_cartesian.add(drift_cartesian);
			}
		}
		
		Vector2d average_drift_cartesian = null;
		PolarVector average_drift_polar = null;
		int drift_vectors_count;
		do {
			average_drift_cartesian = new Vector2d(0, 0);
			drift_vectors_count = drift_vectors_cartesian.size();
			
			//Get average cartesian drift vector, and convert back to polar
			for (Vector2d drift_cartesian : drift_vectors_cartesian) {
				average_drift_cartesian.add(drift_cartesian); 
			}
			average_drift_cartesian.scale(1.0 / drift_vectors_count); 
			
			double average_drift_bearing = Math.atan2(average_drift_cartesian.y, average_drift_cartesian.x);
			average_drift_bearing = Math.toDegrees(average_drift_bearing);
			
			double average_drift_size = Math.sqrt(
					average_drift_cartesian.x * average_drift_cartesian.x + 
					average_drift_cartesian.y * average_drift_cartesian.y);
			
			average_drift_polar = new PolarVector(average_drift_bearing, average_drift_size);
			
			//discard outliers, one at a time
			for (int i = 0; i < drift_vectors_count; i++) {
				PolarVector drift_vector_polar = drift_vectors_polar.get(i);
				
				boolean size_cutoff = Math.abs(drift_vector_polar.size - average_drift_polar.size) > DRIFT_OUTLIER_CUTOFF_SIZE;
				boolean bearing_cutoff = Math.abs(
						FlightAnalyser.calcBearingChange(drift_vector_polar.bearing, average_drift_polar.bearing)) > 
						DRIFT_OUTLIER_CUTOFF_BEARING;
				if (size_cutoff || bearing_cutoff) {
					drift_vectors_cartesian.remove(i);
					drift_vectors_polar.remove(i);
					break; //Go back to recalculate the average - doing these one at a time should get more accurate results
				}
			}
		} while (drift_vectors_count > drift_vectors_cartesian.size() && drift_vectors_cartesian.size() > 0);
		
		if (drift_vectors_cartesian.size() > 0) {
			//now we have a wind vector, except that it's size is in "meters per circle"
			average_drift_polar.size = 1.0 * average_drift_polar.size / t.getAverageCircleDuration();
			t.wind = average_drift_polar;
		} else {
			// No wind to speak of.  At least avoid NPEs down the line
			t.wind = new PolarVector(0, 0);
		}
		
		return t;
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
		for (Circle c2 : t.getCircles()) {
			if (c1 != null) {
				GNSSPoint c1_start = c1.getStartPoint();
				GNSSPoint c2_start = c2.getStartPoint();
				
				double drift_bearing = FlightAnalyser.calculateTrackCourse(c1_start , c2_start);
				double drift_distance = c1_start.distance(c2_start);
				
				c2.drift_vector = new PolarVector(drift_bearing, drift_distance); 			
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
