package com.polymorph.soaringcoach;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.polymorph.soaringcoach.analysis.PolarVector;

public class Thermal {
	public ArrayList<Circle> circles;
	public PolarVector wind = null;
	
	/**
	 * Make a new thermal, ready to accept turns
	 */
	public Thermal() {
		this.circles = new ArrayList<Circle>();
	}

	/**
	 * Make a single-circle thermal.  Allows adding more circles if needed.
	 */
	public Thermal(Circle t) {
		this.circles = new ArrayList<Circle>();
		circles.add(t);
	}
	
	public void addTurn(Circle t) {
		circles.add(t);
	}
	
	public ArrayList<Circle> getCircles() {
		return circles;
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
