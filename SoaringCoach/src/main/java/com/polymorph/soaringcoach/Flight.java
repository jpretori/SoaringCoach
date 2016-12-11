package com.polymorph.soaringcoach;

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
	public boolean is_distance_analysis_complete = false;
	public boolean is_wind_analysis_complete = false;
	
	public double total_track_distance = 0;
	public ArrayList<GNSSPoint> igc_points;
	
	public boolean is_circling_analysis_complete = false;
	public ArrayList<Circle> circles;
	
	public boolean is_thermal_analysis_complete;
	public ArrayList<Thermal> thermals = null;
	
	public boolean is_centring_analysis_complete;
}
