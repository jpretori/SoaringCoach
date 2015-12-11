package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

public class FlightAnalyser {
	ArrayList<String> igc_file = new ArrayList<>();
	ArrayList<GNSSPoint> igc_points = new ArrayList<>();
	
	public FlightAnalyser(ArrayList<GNSSPoint> file) {
		igc_points = file;
	}
	
	public double calcTotalDistance() {
		double total_dist = 0;
		GNSSPoint prev_pt = null;
		for (GNSSPoint pt : igc_points) {
			prev_pt = pt;
			
			if (pt != null && prev_pt != null) {
				total_dist += pt.distance(prev_pt);
			}
		}
		return total_dist;
	}
}
