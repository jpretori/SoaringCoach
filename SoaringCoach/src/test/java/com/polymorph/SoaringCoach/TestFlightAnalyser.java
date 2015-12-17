package com.polymorph.soaringcoach;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestFlightAnalyser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFlightAnalyser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCalcTotalDistance() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1751253339800S01924950EA0450004450"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1751263359917S01917417EA0449504445"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1751273402883S02029017EA0449004430"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(149100, fa.calcTotalDistance(), 100);
	}

}
