package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.Thermal;

public class TestCentringAnalysis {

	/**
	 * Positive: Correctly determine whether or not the fluctuations in bearing
	 * between a set of circles, indicate that a correction move has occurred.
	 * This is about testing the calculations that come up with the conclusion
	 * that there was a fluctuation in circle drift in the sense of a change in
	 * bearing. Also check that the drift direction and distance indication is
	 * correct.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCorrectionDetectionBearingChanged() throws Exception {
		
		PolarVector[] expect_corrections = {
				new PolarVector(0, 0),
				new PolarVector(81.6, 278), 
				new PolarVector(79.6, 295),
				new PolarVector(68.0, 194)};
		
		Flight f = new Flight();
		f.igc_points = FlightAnalyserTestFacade.loadFromFile("src/test/resources/testCorrectionDetectionBearingChanged.igc");
		
		CirclingAnalysis circ_a = new CirclingAnalysis();
		f = circ_a.performAnalysis(f);
		
		Thermal t = new Thermal();
		f.thermals = new ArrayList<>(); 
		f.thermals.add(t);
		t.wind = new PolarVector(0, 0);
		for (Circle c : f.circles) {
			t.addCircle(c);
		}
		f.is_thermal_analysis_complete = true;
		f.is_wind_analysis_complete = true;
		
		CentringAnalysis cen_a = new CentringAnalysis();
		f = cen_a.performAnalysis(f);
		
		assertEquals("Number of circles", 4, f.circles.size());
		
		int i = 0;
		for (Circle circle : f.circles) {
			assertEquals(
					"Circle at index " + i, 
					expect_corrections[i].size, 
					circle.correction_vector.size,
					1);
			
			assertEquals(
					"Circle at index " + i, 
					expect_corrections[i].bearing, 
					circle.correction_vector.bearing,
					0.1);
			i += 1;
		}
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		Flight f = new Flight();
		f.igc_points = new ArrayList<>();
		IAnalysis ca = new CentringAnalysis();
		
		boolean got_exception = false;
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		got_exception = false;
		CirclingAnalysis circ_a = new CirclingAnalysis();
		f = circ_a.performAnalysis(f);
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		got_exception = false;
		ThermalAnalysis ta = new ThermalAnalysis();
		f = ta.performAnalysis(f);
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		WindAnalysis wa = new WindAnalysis();
		f = wa.performAnalysis(f);

		TestUtilities.testHasBeenRun(ca, f);
	}
	
	@Test
	public void testCalcDestinationPoint() {
		CentringAnalysis ca = new CentringAnalysis();
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", null, 0, 0, null, 0, 0, null);
		PolarVector wind = new PolarVector(0, 2.224);
		GNSSPoint p2 = ca.calcDestinationPoint(p1, wind, 25);
		
		assertEquals(0, p2.getLongitude(), 0.00001);
		assertEquals(0.0005, p2.getLatitude(), 0.00001);
	}

}
