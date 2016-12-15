package com.polymorph.soaringcoach;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

import com.polymorph.soaringcoach.analysis.GNSSPoint;

/**
 * Contains any and all detail about the flight, including the raw data as well
 * as the results of all analyses. Will eventually be able to persist itself
 * using a data store. Can be given to a UI for display and interaction
 * purposes.
 * 
 * @author johanpretorius
 *
 */
public class Flight {
	public long id = 0;
	
	public ArrayList<GNSSPoint> igc_points;
	
	public boolean is_distance_analysis_complete = false;
	public double total_track_distance = 0;
	
	public boolean is_circling_analysis_complete = false;
	public ArrayList<Circle> circles;
	
	public boolean is_wind_analysis_complete = false;

	public boolean is_thermal_analysis_complete;
	public ArrayList<Thermal> thermals = null;
	
	public boolean is_centring_analysis_complete;
	
	/**
	 * Creates a new Flight object, initialised with the fixes provided - ready for analysis
	 * @param fixes
	 */
	protected Flight(ArrayList<GNSSPoint> fixes) {
		this.igc_points = fixes;
	}
	
	/**
	 * Creates a FlightSummary object based on the values contained in this class
	 * 
	 * @return FlightSummary object
	 */
	public FlightSummary getFlightSummary() {
		FlightSummary fs = new FlightSummary(this.id, this.total_track_distance);
		
		return fs;
	}

	public long getDuration() {
		long first_fix = igc_points.get(0).data.timestamp.getTime();
		long last_fix = igc_points.get(igc_points.size()-1).data.timestamp.getTime();
		
		long seconds = (last_fix - first_fix) / 1000;
		
		return seconds;
	}
}
