package com.polymorph.soaringcoach;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestCalculateTrackCourse {

	@Test
	public void testNNE() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1, 1, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 2, 1.01, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(0.5725, crs, 0.001);
	}

	@Test
	public void testNNW() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1, 1, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 2, 0.99, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(359.4272, crs, 0.001);
	}

	@Test
	public void testNE() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1.00, 1.00, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 1.01, 1.01, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(44.996, crs, 0.001);
	}

	@Test
	public void testN() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1.00, 1.00, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 1.01, 1.00, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(0.000, crs, 0.001);
	}
}
