package com.polymorph.soaringcoach;

import java.util.Date;

import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class CircleTestFacade extends Circle {
	public CircleTestFacade(
			Date timestamp, 
			long duration,
			double circle_start_latitude,
			double circle_start_longitude,
			double circle_drift_bearing) {
		super(timestamp, duration, circle_start_latitude, circle_start_longitude);
	}

	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, FlightMode mode) {
		super(p1, p2, mode);
	}
	
	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, CircleTestFacade c) {
		super(p1, p2, c);
	}
	
	public boolean detectCircleCompleted(GNSSPoint p) {
		return super.detectCircleCompleted(p);
	}
	
}
