package com.polymorph.soaringcoach.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.polymorph.soaringcoach.CHECK_TWICE_RULE;
import com.polymorph.soaringcoach.COMPASS_POINTS;

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
	
	public String toString() {
		String duration_s = duration < 10 ? "0" + String.valueOf(duration) : String.valueOf(duration);
		
		return "Duration (s) = ["+duration_s+"]";
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

}