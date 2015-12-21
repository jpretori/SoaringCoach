package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

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
	 * @return
	 */
	public double getAverageTurnRate() {
		double average_turn_rate = 0;
		
		for (Turn turnData : turns) {
			average_turn_rate += turnData.duration;
		}
		average_turn_rate = average_turn_rate / turns.size();
		
		return average_turn_rate;
	}
}
