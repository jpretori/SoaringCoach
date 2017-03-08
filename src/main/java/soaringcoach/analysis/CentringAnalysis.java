/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach.analysis;

import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.FlightAnalyser;
import soaringcoach.Thermal;

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
public class CentringAnalysis extends AAnalysis {
	/**
	 * Only correction vectors longer than this value are recognized as such.
	 */
	public static final long CORRECTION_VECTOR_RECOGNITION_THRESHOLD = 5;

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		
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
							calcDestinationPoint(previous_circle.getStartPoint(), t.wind, previous_circle.duration);
					
					double correction_bearing = FlightAnalyser.calculateTrackCourse(
							expected_circle_start_point, circle.getStartPoint());
					
					double correction_distance = expected_circle_start_point.distance(circle.getStartPoint());
					
					PolarVector correction = new PolarVector(correction_bearing, correction_distance);
					
					if (correction.size > CORRECTION_VECTOR_RECOGNITION_THRESHOLD) {
						circle.correction_vector = correction;
					} else {
						circle.correction_vector = new PolarVector(0, 0);
					}
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

	
	GNSSPoint calcDestinationPoint(GNSSPoint p1, PolarVector wind, long circle_duration) {
		final double R = 6371000.0; //Earth mean radius in meters
		
		double d = wind.size * circle_duration; //distance we expect the wind to push us during the whole circle
		double brng = Math.toRadians(wind.bearing);
		
		double latitude = Math.asin(Math.sin(p1.lat_radians) * Math.cos(d/R) +
                Math.cos(p1.lat_radians) * Math.sin(d/R) * Math.cos(brng));
		
		double longitude = p1.lon_radians + Math.atan2(Math.sin(brng) * Math.sin(d/R) * Math.cos(p1.lat_radians),
                     Math.cos(d/R) - Math.sin(p1.lat_radians) * Math.sin(latitude));
		
		double lat_deg = Math.toDegrees(latitude);
		double lon_deg = Math.toDegrees(longitude);
		GNSSPoint p2 = GNSSPoint.createGNSSPoint(null, null, lat_deg, lon_deg, null, 0, 0, null);
		
		return p2;
	}
}
