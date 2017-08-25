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

package soaringcoach;

import java.text.SimpleDateFormat;
import java.util.Date;

import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.PolarVector;

public class Circle {
	public Date timestamp;
	public long duration;
	private boolean included_in_thermal = false;
	CHECK_TWICE_RULE check_twice_rule_followed = CHECK_TWICE_RULE.NOT_APPLICABLE;
	
	private double circle_start_latitude;
	private double circle_start_longitude;

	public double circle_start_course = -400;
	public double deg_course_change_since_start = 0;
	public boolean circle_completed = false;
	public FlightMode turn_direction = FlightMode.CRUISING;
	
	public PolarVector drift_vector = null;
	
	/**
	 * By how much did the pilot (intentionally or not) move this circle
	 * relative to the previous one? This vector already has calculated wind
	 * influence subtracted out.
	 */
	public PolarVector correction_vector = null;
	public GNSSPoint startPoint;
	public GNSSPoint endPoint = null;

	/**
	 * @param p1 GNSS Point.  Must be resolved.
	 * @param p2 GNSS Point.  Must be resolved.
	 * @param mode 
	 * 
	 */
	public Circle(GNSSPoint p1, GNSSPoint p2, FlightMode mode) {
		this.startPoint = p1;
		this.timestamp = p2.data.timestamp;
		this.circle_start_latitude = p2.getLatitude();
		this.circle_start_longitude = p2.getLongitude();	
		this.turn_direction = mode;
		this.circle_start_course = p1.getBearingIntoPoint();
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
			double circle_start_longitude) {
		this.timestamp = timestamp;
		this.duration = duration;
		this.circle_start_latitude = circle_start_latitude;
		this.circle_start_longitude = circle_start_longitude;
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

	public int getAltitudeChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getClimbRate() {
		// TODO Auto-generated method stub
		return 0;
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
	
	public String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				
		return sdf.format(timestamp);
	}

	public GNSSPoint getStartPoint() {
		GNSSPoint start_point = GNSSPoint.createGNSSPoint(null, timestamp, circle_start_latitude,
				circle_start_longitude, "A", 0, 0, null);
		
		return start_point ;
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
			 double angle = Math.abs(circle_start_course - p.getBearingIntoPoint());
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
		
		this.endPoint  = p;
		
		return circle_completed;
	}
}