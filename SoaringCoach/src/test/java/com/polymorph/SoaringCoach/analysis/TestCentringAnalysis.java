package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.COMPASS_POINTS;
import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;

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
		int[] expect_circle_correction_distance = { 0, 18, 38, 13 };
		
		double[] expect_circle_correction_direction = {
				0, 0, 0, 0};
		
		Flight f = new Flight();
		f.igc_points = FlightAnalyserTestFacade.loadFromFile("src/test/resources/testCorrectionDetectionBearingChanged.igc");
		
		CirclingAnalysis circ_a = new CirclingAnalysis();
		f = circ_a.performAnalysis(f);
		
		ThermalAnalysis ta = new ThermalAnalysis();
		f = ta.performAnalysis(f);
		
		WindAnalysis wa = new WindAnalysis();
		f = wa.performAnalysis(f);
		
		CentringAnalysis cen_a = new CentringAnalysis();
		f = cen_a.performAnalysis(f);
		
		assertEquals("Number of circles", 4, f.circles.size());
		
		int i = 0;
		for (Circle circle : f.circles) {
			assertEquals(
					"Circle at index " + i, 
					expect_circle_correction_distance[i], 
					circle.correction_vector.size,
					0.1);
			
			assertEquals(
					"Circle at index " + i, 
					expect_circle_correction_direction[i], 
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

}
