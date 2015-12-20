package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

public class Turns {
	private ArrayList<TurnData> turns;
	
	public enum FlightMode {
		TURNING_LEFT, 
		TURNING_RIGHT, 
		CRUISING
	};
	
	public Turns() {
		this.turns = new ArrayList<TurnData>();
	}
	
	public void addTurn(TurnData t) {
		turns.add(t);
	}
	
	public ArrayList<TurnData> getTurns() {
		return turns;
	}
	
	/**
	 * Calculates and returns the average turn rate over the entire set
	 * @return
	 */
	public double getAverageTurnRate() {
		double average_turn_rate = 0;
		
		for (TurnData turnData : turns) {
			average_turn_rate += turnData.duration;
		}
		average_turn_rate = average_turn_rate / turns.size();
		
		return average_turn_rate;
	}
}
