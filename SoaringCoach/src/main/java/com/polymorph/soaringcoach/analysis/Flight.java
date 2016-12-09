package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

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
	public boolean is_distance_analysis_complete = false;
	public boolean is_wind_analysis_complete = false;
	
	public ArrayList<Thermal> thermals = null;
	public double total_track_distance = 0;
	public ArrayList<GNSSPoint> igc_points;
}
