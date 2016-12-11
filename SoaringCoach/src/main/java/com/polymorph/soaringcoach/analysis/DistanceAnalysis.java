package com.polymorph.soaringcoach.analysis;

import com.polymorph.soaringcoach.Flight;

/**
 * Works out the task distance, flight distance, as well as the track deviation percentage
 * 
 * @author johanpretorius
 *
 */
public class DistanceAnalysis implements IAnalysis {

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
		double total_dist = 0;
		GNSSPoint prev_pt = null;
		if (flight.igc_points != null) {				
			for (GNSSPoint pt : flight.igc_points) {
	
				if (pt != null && prev_pt != null) {
					total_dist += pt.distance(prev_pt);
				}
				prev_pt = pt;
			}
		}
		flight.total_track_distance = total_dist;
		
		flight.is_distance_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_distance_analysis_complete;
	}

}
