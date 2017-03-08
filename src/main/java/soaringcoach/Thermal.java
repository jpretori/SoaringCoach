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
import java.util.ArrayList;
import java.util.Date;

import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.PolarVector;

public class Thermal {
	public ArrayList<Circle> circles;
	public PolarVector wind = null;
	public boolean could_not_calculate_wind = false;
	public GNSSPoint startPoint;
	public GNSSPoint endPoint;
	
	/**
	 * Make a new thermal, ready to accept turns
	 */
	public Thermal() {
		this.circles = new ArrayList<Circle>();
	}

	/**
	 * Make a single-circle thermal.  Allows adding more circles if needed.
	 */
	public Thermal(Circle c) {
		this.circles = new ArrayList<>();
		circles.add(c);
		this.startPoint = c.startPoint;
		this.endPoint = c.endPoint;
	}
	
	/**
	 * Figures out whether <code>Circle c</code> can be attached to the thermal,
	 * by checking circle durations and start times. If it matches, this will
	 * add the circle to the thermal and return <code>true</code>. If not, the
	 * circle will *not* be added, and return <code>false</code>
	 * 
	 * @param c
	 * @return <code>true</code> if the circle was added to the thermal
	 * <br><code>false</code> otherwise
	 */
	public boolean addCircle(Circle c) {
		
		if (this.circles.isEmpty()) {
			this.circles.add(c);
			this.startPoint = c.startPoint;
			this.endPoint = c.endPoint;
			return  true;
		}
		
		Circle lastCircle = this.circles.get(this.circles.size() - 1);
		if ((lastCircle.timestamp.getTime() + lastCircle.duration*1000) == c.timestamp.getTime()) {
			circles.add(c);
			this.endPoint = c.endPoint;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Calculates and returns the average turn rate over the entire set
	 * @return a long value - expecting people to get more value out of whole number seconds
	 */
	public long getAverageCircleDuration() {
		double avg_circle_duration = 0;
		
		for (Circle c : circles) {
			avg_circle_duration += c.duration;
		}
		avg_circle_duration = avg_circle_duration / circles.size();
		
		return Math.round(avg_circle_duration);
	}
	
	public String getTotalDuration() {
		long total_seconds = 0;
		
		for (Circle c : circles) {
			total_seconds += c.duration;
		}
		
		long minutes = total_seconds / 60;
		long seconds = total_seconds % 60;
		
		String time = minutes + ":" + (
				seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		
		return time;
	}
	
	public long getTotalDurationSeconds() {
		long total_seconds = 0;
		
		for (Circle c : circles) {
			total_seconds += c.duration;
		}
		
		return total_seconds;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date first_circle_timestamp = circles.get(0).timestamp;
		return 
				"Start Time = ["+df.format(first_circle_timestamp)+"], "
				+ "Thermal Duration (min:sec) = ["+getTotalDuration()+"], "
				+ "Average circle duration (s) = ["+getAverageCircleDuration()+"], "
				+ "Number of circles = ["+circles.size()+"]";
	}
}
