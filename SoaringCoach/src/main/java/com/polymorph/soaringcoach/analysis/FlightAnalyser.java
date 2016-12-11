package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import com.polymorph.soaringcoach.CHECK_TWICE_RULE;
import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Thermal;

public class FlightAnalyser {
	public enum FlightMode {
		TURNING_LEFT, 
		TURNING_RIGHT, 
		CRUISING
	}

	
	private final double MAX_AVERAGE_DEVIATION_DRIFT_DISTANCE = 10.0;
	private final double MAX_AVERAGE_DEVIATION_DRIFT_BEARING = 5.0;
	
	private ArrayList<GNSSPoint> igc_points = new ArrayList<>();
	
	public FlightAnalyser(ArrayList<GNSSPoint> file) {
		igc_points = file;
	}
	
	/**
	 * Get the total distance flown over ground by adding together the 
	 * distance between all points in the file.
	 * @return total distance flown
	 */
	public double calcTotalDistance() {
		return 0;
	}

	/**
	 * Looks at all the turns identified by calculateTurnRates(), and identifies
	 * which ones fit together into thermals. Also calculates some aggregates
	 * for each thermal.
	 * 
	 * @return ArrayList<Thermal>
	 * @throws Exception 
	 */
	public ArrayList<Thermal> calculateThermals() throws Exception {
		ArrayList<Thermal> thermals = new ArrayList<>();
		return thermals;
	}
	
	public ArrayList<Circle> analyseCircling() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	/**
	 * Helper to calculate the bearing to get from p1 to p2
	 * 
	 * @param p1
	 * @param p2
	 * @return double - bearing in degrees
	 */
	static double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		double y = Math.sin(p2.lon_radians - p1.lon_radians) * 
				Math.cos(p2.lat_radians);
		
		double x = Math.cos(p1.lat_radians) * Math.sin(p2.lat_radians) -
		        Math.sin(p1.lat_radians) * Math.cos(p2.lat_radians) *
		        Math.cos(p2.lon_radians - p1.lon_radians);
		
		double track_radians = Math.atan2(y, x);
		double track_degrees = Math.toDegrees(track_radians);
		track_degrees = (track_degrees + 360) % 360;
		return track_degrees;
	}

	/**
	 * NPE-avoiding call-through to calculate bearing from a circle's start-point to another given point.
	 * 
	 * @param c
	 * @param p2
	 * @return
	 */
	static double calculateTrackCourse(Circle c, double lat, double lon) {
		double course = -1;
		if (c != null) {
			GNSSPoint p = GNSSPoint.createGNSSPoint(null, null, lat, lon, null, 0, 0, null);
			course = calculateTrackCourse(c.getStartPoint(), p);
		}
		return course;
	}

	/**
	 * Helper to figure out the change in track course from one point's track course
	 * to the next
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	static double calcBearingChange(double crs1, double crs2) {
		double r = crs2 - crs1;
		
		if (Math.abs(r) >= 180) {
			if (crs2 < crs1) {
				r += 360;
			} 
			else {
				r -= 360;
			}
		}

		return r;
	}

	protected void setIgcPoints(ArrayList<GNSSPoint> points) {
		this.igc_points = points;
	}

	protected void checkTwiceRule(ArrayList<Circle> circles) {
		boolean previous_circle_correction = true;
		for (Circle circle : circles) {
			if (circle.centeringCorrection()) {
				if (previous_circle_correction) {
					circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.NOT_FOLLOWED);
				} else {
					circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.FOLLOWED);
				}
			} else {
				circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.NOT_APPLICABLE);
			}
			previous_circle_correction = circle.centeringCorrection();
		}
	}
	
	protected GNSSPoint calcDestinationPoint(GNSSPoint p1, double brng, double d) {
		final double R = 6371000.0; //Earth mean radius in meters
		
		double latitude = Math.asin(Math.sin(p1.lat_radians) * Math.cos(d / R) +
                Math.cos(p1.lat_radians) * Math.sin(d/R) * Math.cos(brng) );
		
		double longitude = p1.lon_radians + Math.atan2(Math.sin(brng) * Math.sin(d/R) * Math.cos(p1.lat_radians),
                     Math.cos(d/R) - Math.sin(p1.lat_radians) * Math.sin(latitude));
		
		GNSSPoint p2 = GNSSPoint.createGNSSPoint(null, null, Math.toDegrees(latitude), Math.toDegrees(longitude), null, 0, 0, null);
		
		return p2;
	}

	/**
	 * 		Iterate through circles and work out average circle drift distance
		and average circle drift bearing, trimming away outliers until
		average deviation is single digits for distance and <5 degrees for
		direction.  This gets us a trend of circle drift direction & distance, 
		which approximates wind direction.
<br><br>
		Iterate through again - for each circle, calculate first where the
		average distance and bearing indicates we would have started this
		circle. While there, calculate the bearing and distance from this
		expected circle start point to the actual start point. This is the
		correction vector, i.e. pilot or turbulence induced changes to circle drift.

	 * @param circles
	 * @return
	 */
	ArrayList<Circle> calculateCorrectionVectors(ArrayList<Circle> circles) {

		Circle previous_circle = null;
		double average_drift_distance = 0;
		double average_drift_bearing = 0;
		int i = 0;
		for (Circle circle : circles) {
			if (circle != null && previous_circle != null) {
				
				GNSSPoint p1 = previous_circle.getStartPoint();
				GNSSPoint p2 = circle.getStartPoint();
				
				circle.circle_drift_bearing = calculateTrackCourse(p1, p2);
				average_drift_bearing += circle.circle_drift_bearing;
				
				circle.circle_drift_distance = p1.distance(p2);
				average_drift_distance += circle.circle_drift_distance;
			}
			
			previous_circle = circle;
			i += 1;
		}
		average_drift_distance = average_drift_distance / i;
		average_drift_bearing = average_drift_bearing / i;

		// Trim away distance and bearing values that fall outside the required
		// average deviation parameters
		previous_circle = null;
		for (Circle circle : circles) {
			if (circle != null && previous_circle != null) {
				if (Math.abs(circle.getCircleDriftBearing() - average_drift_bearing) > MAX_AVERAGE_DEVIATION_DRIFT_BEARING ||
						Math.abs(circle.getCircleDriftDistance() - average_drift_distance) > MAX_AVERAGE_DEVIATION_DRIFT_DISTANCE) {
					
					average_drift_bearing = average_drift_bearing * i;
					average_drift_bearing -= circle.getCircleDriftBearing();
					average_drift_distance = average_drift_distance * i;
					average_drift_distance -= circle.getCircleDriftDistance();
					i -= 1;
					average_drift_bearing = average_drift_bearing / i;
					average_drift_distance = average_drift_distance / i;
				}
			}
			previous_circle = circle;
		}
		
		// Now work out where the average drift makes us expect each circle, and
		// work out the correction vector that takes us from that point to the
		// one where the circle actually started
		
		for (Circle circle : circles) {
			if (circle != null && previous_circle != null) {
				GNSSPoint expected_circle_start_point = 
						calcDestinationPoint(previous_circle.getStartPoint(), average_drift_bearing, average_drift_distance);
				
				double correction_bearing = calculateTrackCourse(expected_circle_start_point, circle.getStartPoint());
				circle.setCorrectionBearing(correction_bearing);
				
				double correction_distance = expected_circle_start_point.distance(circle.getStartPoint());
				circle.setCorrectionDistance(correction_distance);
			}		
			previous_circle = circle;
		}
		
		return circles;
	}
}
