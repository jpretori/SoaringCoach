package com.polymorph.soaringcoach.analysis;

import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;

public class CircleTestFacade extends Circle {
	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, FlightMode mode) {
		super(p1, p2, mode);
	}
	
	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, CircleTestFacade c) {
		super(p1, p2, c);
	}

	public boolean detectCircleCompleted(GNSSPoint p) throws Exception {
		return super.detectCircleCompleted(p);
	}
}
