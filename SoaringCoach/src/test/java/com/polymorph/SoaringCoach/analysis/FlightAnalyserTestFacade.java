package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

public class FlightAnalyserTestFacade extends FlightAnalyser {
	
	public FlightAnalyserTestFacade(ArrayList<GNSSPoint> file) {
		super(file);
	}

	public double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		return super.calculateTrackCourse(p1, p2);
	}
}
