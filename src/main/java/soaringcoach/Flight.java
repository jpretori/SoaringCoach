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

import java.util.ArrayList;

import soaringcoach.analysis.GNSSPoint;

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
	
	public boolean is_circles_analysis_complete = false;
	public ArrayList<Circle> circles;
	
	public boolean is_wind_analysis_complete = false;

	public boolean is_thermal_analysis_complete;
	public ArrayList<Thermal> thermals = null;
	
	public boolean is_centring_analysis_complete;

	public String pilot_name;

	public boolean is_short_straight_phases_analysis_complete = false;
	public ArrayList<StraightPhase> straight_phases;

	public boolean isFlightDebriefingAnalysisComplete = false;
	public FlightDebriefing flightDebriefing;

	public boolean is_circles_percentage_analysis_complete = false;

	public double percentageTimeCircling = -1.0;

	public String flightDate;
	
	/**
	 * Creates a new Flight object, initialised with the fixes provided - ready for analysis
	 * @param fixes
	 */
	protected Flight(ArrayList<GNSSPoint> fixes) {
		this.igc_points = fixes;
	}
	
	/**
	 * Creates a new Flight object
	 * @param fixes
	 */
	protected Flight() {
		this.igc_points = new ArrayList<>();
	}
}
