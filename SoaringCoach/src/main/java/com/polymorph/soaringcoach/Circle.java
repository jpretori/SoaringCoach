package com.polymorph.soaringcoach;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.PolarVector;
import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;

public class Circle {
	public Date timestamp;
	public long duration;
	private boolean included_in_thermal = false;
	CHECK_TWICE_RULE check_twice_rule_followed = CHECK_TWICE_RULE.NOT_APPLICABLE;
	private boolean centring_correction;
	
	private double circle_start_latitude;
	private double circle_start_longitude;
	
	public double circle_drift_bearing;
	public double circle_drift_distance;

	public COMPASS_POINTS correction_direction;
	public double correction_bearing;
	public double correction_distance;
	
	public double circle_start_course = -400;
	public double deg_course_change_since_start = 0;
	public boolean circle_completed = false;
	public FlightMode turn_direction = FlightMode.CRUISING;
	
	public PolarVector drift_vector = null;

	/**
	 * @param p1 GNSS Point.  Must be resolved.
	 * @param p2 GNSS Point.  Must be resolved.
	 * @param mode 
	 * 
	 */
	public Circle(GNSSPoint p1, GNSSPoint p2, FlightMode mode) {
		this.timestamp = p2.data.timestamp;
		this.circle_start_latitude = p2.getLatitude();
		this.circle_start_longitude = p2.getLongitude();	
		this.turn_direction = mode;
		this.circle_start_course = p1.track_course_deg;
	}
	
	
	/**
	 * @param timestamp what time did the circle start (timestamp from IGC file)
	 * @param duration how many seconds did it take to go all the way around
	 * @param circle_start_latitude what latitude was the start position of the circle
	 * @param circle_start_longitude what longitude was the start position of the circle
	 * @param circle_drift_bearing whwere did this circle start compared to the previous one
	 */
	public Circle(
			Date timestamp, 
			long duration,
			double circle_start_latitude,
			double circle_start_longitude,
			double circle_drift_bearing) {
		this.timestamp = timestamp;
		this.duration = duration;
		this.circle_start_latitude = circle_start_latitude;
		this.circle_start_longitude = circle_start_longitude;
		this.circle_drift_bearing = circle_drift_bearing;
	}
	
	/**
	 * Facilitates making a second (and subsequent) circle in the same thermal, while keeping a constant reference heading
	 * @param p1
	 * @param p2
	 * @param previous_circle
	 */
	public Circle(GNSSPoint p1, GNSSPoint p2, Circle previous_circle) {
		this(p1, p2, previous_circle.turn_direction);
		this.circle_start_course = previous_circle.circle_start_course;
	}


	public String toString() {
		String duration_s = duration < 10 ? "0" + String.valueOf(duration) : String.valueOf(duration);
		
		return "Timestamp = [" + this.getTimestamp() + "]; Duration (s) = ["+duration_s+"]";
	}

	public boolean isIncludedInThermal() {
		return included_in_thermal;
	}

	public void setIncludedInThermal() {
		this.included_in_thermal = true;
	}

	public boolean centeringCorrection() {
		return centring_correction;
	}

	public int getAltitudeChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getClimbRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCorrectionDistance() {
		return correction_distance;
	}

	public COMPASS_POINTS getCorrectionDirection() {
		return correction_direction;
	}

	void setCentringCorrection(boolean b) {
		centring_correction = b;
	}

	public CHECK_TWICE_RULE getCheckTwiceRuleIndicator() {
		return check_twice_rule_followed;
	}
	
	public void setCheckTwiceRuleIndicator(CHECK_TWICE_RULE check_twice_rule_followed) {
		this.check_twice_rule_followed = check_twice_rule_followed;
	}
	
	public double getCircleStartLatitude() {
		return circle_start_latitude;
	}

	public double getCircleStartLongitude() {
		return circle_start_longitude;
	}

	public double getCircleDriftBearing() {
		return circle_drift_bearing;
	}
	
	public double getCircleDriftDistance() {
		return circle_drift_distance;
	}

	public String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				
		return sdf.format(timestamp);
	}

	public GNSSPoint getStartPoint() {
		GNSSPoint start_point = GNSSPoint.createGNSSPoint(null, timestamp, circle_start_latitude,
				circle_start_longitude, "A", 0, 0, null);
		
		return start_point ;
	}

	public void setCorrectionBearing(double correction_bearing) {
		this.correction_bearing = correction_bearing;
		if (correction_bearing >= 338 || correction_bearing <= 22) {
			this.correction_direction = COMPASS_POINTS.N;
		} else if (correction_bearing >= 23 && correction_bearing <= 67) {
			this.correction_direction = COMPASS_POINTS.NE;
		} else if (correction_bearing >= 68 && correction_bearing <= 112) {
			this.correction_direction = COMPASS_POINTS.E;
		} else if (correction_bearing >= 113 && correction_bearing <= 157) {
			this.correction_direction = COMPASS_POINTS.SE;
		} else if (correction_bearing >= 158 && correction_bearing <= 202) {
			this.correction_direction = COMPASS_POINTS.S;
		} else if (correction_bearing >= 203 && correction_bearing <= 247) {
			this.correction_direction = COMPASS_POINTS.SW;
		} else if (correction_bearing >= 248 && correction_bearing <= 292) {
			this.correction_direction = COMPASS_POINTS.W;
		} else if (correction_bearing >= 293 && correction_bearing <= 337) {
			this.correction_direction = COMPASS_POINTS.NW;
		}
	}

	public void setCorrectionDistance(double correction_distance) {
		this.correction_distance = correction_distance;
		
	}

	/**
	 * 
	 * @param p the last point in the circle, which by comparison to the start timestamp will determine the duration
	 */
	public void setDuration(GNSSPoint p) {
		this.duration = 
				p.data.timestamp.getTime() - this.timestamp.getTime();
		
		this.duration /= 1000; //In seconds, please...	
	}
	

	/**
	 * Figures out whether or not we've gone all the way around
	 * 
	 * @param p
	 * @return
	 */
	public boolean detectCircleCompleted(GNSSPoint p) {
		if (deg_course_change_since_start == 0) {
			//Work out the smallest angle between the circle start course & the track course leading to p
			 double angle = Math.abs(circle_start_course - p.track_course_deg);
			 double angle_inverse = Math.abs(angle - 360);
			 
			 if (angle < angle_inverse) {
				 deg_course_change_since_start = angle;
			 } else {
				 deg_course_change_since_start = angle_inverse;
			 }
		} else {
			deg_course_change_since_start += Math.abs(p.turn_rate * p.seconds_since_last_fix);
		}
		
		circle_completed = Math.abs(deg_course_change_since_start) >= 360;
		
		return circle_completed;
	}

}