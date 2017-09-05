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

/**
 * Provides basic information about a flight. Allows for efficiently
 * transporting a list of flights to a UI, such that one can be selected and
 * more detail can be requested.
 * 
 * @author johanpretorius
 *
 */
public class FlightDebriefing {	
	/**
	 * Total track distance over ground
	 */
	public double totalGroundTrackDistance = 0;

	public String pilotName;
	
	public ArrayList<StraightPhase> straightPhases;

	public double percentageTimeCircling = -1.0;

	public String flightDate;

	protected FlightDebriefing(long id, double total_track_distance) {
		this.totalGroundTrackDistance = total_track_distance;
	}


	public FlightDebriefing() {
	}
}
