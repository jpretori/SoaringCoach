package com.polymorph.soaringcoach.analysis;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.Thermal;

/**
 * For each circle, works out the direction and distance by which the pilot (or
 * turbulence) moved the circle. Done by looking at circle Ci and using the wind
 * speed & direction for the thermal to predict where circle Ci+1 will start.
 * Comparing the predicted start point for Ci+1 to the actual one recorded, work
 * out a direction and distance (in meters). This vector is the “correction
 * vector” that got applied by the pilot (or turbulence), and is stored in the circle.
 * 
 * @author johanpretorius
 *
 */
public class CentringAnalysis implements IAnalysis {

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
		
		if (flight.circles == null) {
			throw new AnalysisException("Circles analysis needs to be completed before centring analysis can commence");
		}
		
		if (flight.thermals == null) {
			throw new AnalysisException("Thermals analysis needs to be completed before centring analysis can commence");
		}
		
		if (!flight.is_wind_analysis_complete) {
			throw new AnalysisException("Wind analysis needs to be completed before centring analysis can commence");
		}
		
		for (Thermal t : flight.thermals) {
			Circle previous_circle = null;
			for (Circle circle : t.circles) {
				if (circle != null && previous_circle != null) {
					GNSSPoint expected_circle_start_point = 
							calcDestinationPoint(previous_circle.getStartPoint(), t.wind);
					
					double correction_bearing = FlightAnalyser.calculateTrackCourse(
							expected_circle_start_point, circle.getStartPoint());
					
					double correction_distance = expected_circle_start_point.distance(circle.getStartPoint());
					
					PolarVector correction = new PolarVector(correction_bearing, correction_distance);
					
					circle.correction_vector = correction;
				} else {
					//Avoid NPEs down the line
					circle.correction_vector = new PolarVector(0, 0);
				}
				previous_circle = circle;
			}
		}
		
		flight.is_centring_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_centring_analysis_complete;
	}

	
	private GNSSPoint calcDestinationPoint(GNSSPoint p1, PolarVector wind) {
		final double R = 6371000.0; //Earth mean radius in meters
		
		double d = wind.size;
		double brng = wind.bearing;
		
		double latitude = Math.asin(Math.sin(p1.lat_radians) * Math.cos(d  / R) +
                Math.cos(p1.lat_radians) * Math.sin(d/R) * Math.cos(brng));
		
		double longitude = p1.lon_radians + Math.atan2(Math.sin(brng) * Math.sin(d/R) * Math.cos(p1.lat_radians),
                     Math.cos(d/R) - Math.sin(p1.lat_radians) * Math.sin(latitude));
		
		GNSSPoint p2 = GNSSPoint.createGNSSPoint(null, null, Math.toDegrees(latitude), Math.toDegrees(longitude), null, 0, 0, null);
		
		return p2;
	}
}
