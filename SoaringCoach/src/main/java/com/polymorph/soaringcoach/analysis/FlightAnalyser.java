package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

public class FlightAnalyser {
	ArrayList<String> igc_file = new ArrayList<>();
	ArrayList<GNSSPoint> igc_points = new ArrayList<>();
	
	public FlightAnalyser() {
		//TODO replace dummy values with actual file read
		igc_file.add("B1751253339800S01924950EA0450004450");
		igc_file.add("B1751263359917S01917417EA0449504445");
		igc_file.add("B1751273402883S02029017EA0449004430");
	}
	
	public double calcTotalDistance() {
		double total_dist = 0;
		GNSSPoint point = null;
		GNSSPoint prev_point = null;
		for (String igc_line : igc_file) {
			prev_point = point;
			point = new GNSSPoint(igc_line);
			igc_points.add(point);
			
			if (point != null && prev_point != null) {
				total_dist += point.distance(prev_point);
			}
		}
		return total_dist;
	}
}
