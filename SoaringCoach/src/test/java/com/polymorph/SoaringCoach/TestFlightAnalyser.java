package com.polymorph.SoaringCoach;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;

public class TestFlightAnalyser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFlightAnalyser() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalcTotalDistance() {
		FlightAnalyser fa = new FlightAnalyser();
		assertEquals(149100, fa.calcTotalDistance(), 100);
	}

}
