package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;
import java.util.Date;

import com.polymorph.soaringcoach.CHECK_TWICE_RULE;

public class FlightAnalyser {
	public enum FlightMode {
		TURNING_LEFT, 
		TURNING_RIGHT, 
		CRUISING
	}

	// TURN_RATE_THRESHOLD is in degrees per second.  
	// Turning faster than this constitutes a thermal turn.
	private final int TURN_RATE_THRESHOLD = 4; 
	
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
		double total_dist = 0;
		GNSSPoint prev_pt = null;
		for (GNSSPoint pt : igc_points) {

			if (pt != null && prev_pt != null) {
				total_dist += pt.distance(prev_pt);
			}
			prev_pt = pt;
		}
		return total_dist;
	}

	/**
	 * Analyses the given fixes to find full-circle turns and indicate how
	 * well-banked they were, by highlighting how many seconds it took to
	 * complete each turn.
	 * 
	 * @return all turns and how many seconds each took.  If none were found, an empty array.
	 * @throws Exception 
	 */
	public ArrayList<Circle> analyseCircling() throws Exception {
		
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		GNSSPoint p1 = null;

		//If the aircraft is turning, at what heading did the turn start
		double track_course_turn_start = 0;
		FlightMode mode = FlightMode.CRUISING;
		Date circle_start_time = null;
		double circle_start_latitude = 10000;
		double circle_start_longitude = 10000;
		double circle_drift_bearing = 10000;
		Circle previous_circle = null;
		
		for (GNSSPoint p2 : igc_points) {
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = calculateTrackCourse(p1, p2);
				
				long duration = p2.data.timestamp.getTime() - p1.data.timestamp.getTime(); //in milliseconds
				p2.seconds_since_last_fix = duration / 1000; 
				
				double track_course_delta = calcBearingChange(p1.track_course_deg, p2.track_course_deg);
				p2.turn_rate = track_course_delta / p2.seconds_since_last_fix;
				
				switch (mode) {
					case CRUISING:
						//Detect mode change
						if (p2.turn_rate > TURN_RATE_THRESHOLD) {
							mode = FlightMode.TURNING_RIGHT;
							track_course_turn_start = p2.track_course_deg;
							circle_start_time = p2.data.timestamp;
							circle_start_latitude = p2.getLatitude();
							circle_start_longitude = p2.getLongitude();
						} else if (p2.turn_rate < -TURN_RATE_THRESHOLD) {
							mode = FlightMode.TURNING_LEFT;
							track_course_turn_start = p2.track_course_deg;
							circle_start_time = p2.data.timestamp;
							circle_start_latitude = p2.getLatitude();
							circle_start_longitude = p2.getLongitude();
						}
						break;
					case TURNING_LEFT: 
						//Detect mode change
						if (p2.turn_rate > -TURN_RATE_THRESHOLD) {
							//Turning too slowly to still call this a thermal turn
							mode = FlightMode.CRUISING;
							circle_start_time = null;
						}
						if (p2.turn_rate > TURN_RATE_THRESHOLD) {
							//Switched turn direction
							mode = FlightMode.TURNING_RIGHT;
							track_course_turn_start = p2.track_course_deg;
							circle_start_time = p2.data.timestamp;
							circle_start_latitude = p2.getLatitude();
							circle_start_longitude = p2.getLongitude();
						}
						
						//TODO what about the edge case where p1 doesn't have track_course_deg 
						//initialized because it's the first fix in the file.
						
						//Detect going past turn start course.  Only relevant if we're still turning.
						double p1_bearing_to_turn_start = 
								calcBearingChange(p1.track_course_deg, track_course_turn_start);
						
						double p2_bearing_to_turn_start = 
								calcBearingChange(p2.track_course_deg, track_course_turn_start);
						
						if (mode == FlightMode.TURNING_LEFT &&
								(p1_bearing_to_turn_start < 0) && 
								(Math.abs(p1_bearing_to_turn_start) <= 90) &&
								(p2_bearing_to_turn_start > 0) && 
								(Math.abs(p2_bearing_to_turn_start) <= 90)) {
							// This means we've just passed the course on which the turn started.
							// So a full circle is accomplished!
							long circle_duration = 
									p2.data.timestamp.getTime() - circle_start_time.getTime();
							
							circle_duration /= 1000; //In seconds, please...
							circle_drift_bearing = calculateTrackCourse(
									previous_circle, circle_start_latitude, circle_start_longitude);
							Circle circle = new Circle(
									circle_start_time, 
									circle_duration,
									circle_start_latitude, 
									circle_start_longitude, 
									circle_drift_bearing);
							circles.add(circle);
							circle_start_time = p1.data.timestamp;
							circle_start_latitude = p1.getLatitude();
							circle_start_longitude = p1.getLongitude();
							previous_circle = circle;
						}
						break;
					case TURNING_RIGHT:
						//Detect mode change
						if (p2.turn_rate < TURN_RATE_THRESHOLD) {
							//Turning too slowly to still call this a thermal turn
							mode = FlightMode.CRUISING;
							circle_start_time = null;
						}
						if (p2.turn_rate < -TURN_RATE_THRESHOLD) {
							//Switched turn direction
							mode = FlightMode.TURNING_LEFT;
							track_course_turn_start = p2.track_course_deg;
							circle_start_time = p2.data.timestamp;
							circle_start_latitude = p2.getLatitude();
							circle_start_longitude = p2.getLongitude();
						}
						
						//TODO what about the edge case where p1 doesn't have track_course_deg 
						//initialized because it's the first fix in the file.
						
						//Detect going past turn start course.  Only relevant if we're still turning.
						p1_bearing_to_turn_start = calcBearingChange(p1.track_course_deg, track_course_turn_start);
						p2_bearing_to_turn_start = calcBearingChange(p2.track_course_deg, track_course_turn_start);
						if (mode == FlightMode.TURNING_RIGHT && 
								(p1_bearing_to_turn_start > 0) && 
								(Math.abs(p1_bearing_to_turn_start) <= 90) &&
								(p2_bearing_to_turn_start < 0) && 
								(Math.abs(p2_bearing_to_turn_start) <= 90)) {
							// This means we've just passed the course on which the turn started.
							// So a full circle is accomplished!
							long circle_duration = 
									p2.data.timestamp.getTime() - circle_start_time.getTime();
							circle_duration /= 1000; //In seconds, please...
							
							circle_drift_bearing = calculateTrackCourse(
									previous_circle, circle_start_latitude, circle_start_longitude);  
							Circle circle = new Circle(
									circle_start_time, 
									circle_duration,
									circle_start_latitude, 
									circle_start_longitude, 
									circle_drift_bearing);
							circles.add(circle);
							circle_start_time = p1.data.timestamp;
							circle_start_latitude = p1.getLatitude();
							circle_start_longitude = p1.getLongitude();
							previous_circle = circle;
						}
						
						break;
					default: throw new Exception("Unexpected flight mode indicator [" + mode + "]");
				}
			}
			
			//transfer the pointer so we scan through the list looking at 2 adjacent points all the time
			p1 = p2; 
		}
		
		return circles;
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
		Thermal thermal = null;
		Circle c1 = null;
		ArrayList<Circle> circles = analyseCircling();
		
		for (Circle c2 : circles ) {
			
			if (c1 != null && c2 != null) {
				
				if ((c1.timestamp.getTime() + c1.duration*1000) == c2.timestamp.getTime()) {
					//Turns are adjacent
					
					if (thermal == null) {
						thermal = new Thermal(c1);
						thermals.add(thermal);
						c1.setIncludedInThermal();
					} 
					
					thermal.addTurn(c2);
					c2.setIncludedInThermal();
				} else {
					// Set thermal=null to make sure we initialize a new thermal
					// next time two turns are adjacent 
					thermal = null;
				}
				
				if (!c1.isIncludedInThermal()) {
					thermals.add(new Thermal(c1));
					c1.setIncludedInThermal();
				}
			}
			c1 = c2; // Switch over the pointer so we scan the list looking at two adjacent items
		}
		
		return thermals;
	}
	
	/**
	 * Helper to calculate the bearing to get from p1 to p2
	 * 
	 * @param p1
	 * @param p2
	 * @return double - bearing in degrees
	 */
	protected double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
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
	private double calculateTrackCourse(Circle c, double lat, double lon) {
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
	private double calcBearingChange(double crs1, double crs2) {
		
		double d = Math.abs(crs2 - crs1) % 360;
		
		double r = d > 180 ? 360 - d : d;
		
		int sign = (crs1 - crs2 >= 0 && crs1 - crs2 <= 180) || 
				(crs1 - crs2 <= -180 && crs1 - crs2 >= -360) ? 1 : -1;

		r *= sign;
		
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
					circle.check_twice_rule_followed = CHECK_TWICE_RULE.NOT_FOLLOWED;
				} else {
					circle.check_twice_rule_followed = CHECK_TWICE_RULE.FOLLOWED;
				}
			} else {
				circle.check_twice_rule_followed = CHECK_TWICE_RULE.NOT_APPLICABLE;
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
		direction
<br><br>
		Iterate through again - for each circle, calculate first where the
		average distance and bearing indicates we would have started this
		circle. While there, calculate the bearing and distance from this
		expected circle start point to the actual start point. This is the
		correction vector.

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
