package com.polymorph.soaringcoach.analysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Thermal {
	private ArrayList<Turn> turns;
	
	/**
	 * Make a new thermal, ready to accept turns
	 */
	public Thermal() {
		this.turns = new ArrayList<Turn>();
	}

	/**
	 * Make a single-turn thermal.  Allows adding more turns if needed.
	 */
	public Thermal(Turn t) {
		this.turns = new ArrayList<Turn>();
		turns.add(t);
	}
	
	public void addTurn(Turn t) {
		turns.add(t);
	}
	
	public ArrayList<Turn> getTurns() {
		return turns;
	}
	
	/**
	 * Calculates and returns the average turn rate over the entire set
	 * @return a long value - expecting people to get more value out of whole number seconds
	 */
	public long getAverageCircleDuration() {
		double average_turn_rate = 0;
		
		for (Turn turnData : turns) {
			average_turn_rate += turnData.duration;
		}
		average_turn_rate = average_turn_rate / turns.size();
		
		return Math.round(average_turn_rate);
	}
	
	public String getTotalDuration() {
		long total_seconds = 0;
		
		for (Turn turn : turns) {
			total_seconds += turn.duration;
		}
		
		long minutes = total_seconds / 60;
		long seconds = total_seconds % 60;
		
		String time = minutes + ":" + (
				seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		
		return time;
	}
	
	public long getTotalDurationSeconds() {
		long total_seconds = 0;
		
		for (Turn turn : turns) {
			total_seconds += turn.duration;
		}
		
		return total_seconds;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date first_turn_timestamp = turns.get(0).timestamp;
		return 
				"Start Time = ["+df.format(first_turn_timestamp)+"], "
				+ "Thermal Duration (min:sec) = ["+getTotalDuration()+"], "
				+ "Average circle duration (s) = ["+getAverageCircleDuration()+"], "
				+ "Number of circles = ["+turns.size()+"]";
	}
}
