package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.AnalysisException;
import com.polymorph.soaringcoach.analysis.CirclingAnalysis;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.TestUtilities;
import com.polymorph.soaringcoach.analysis.ThermalAnalysis;

public class TestThermalAnalysis {

	/**
	 * Takes a section of a real IGC file with a number of thermals in evidence,
	 * and ensures the thermals are picked up correctly.
	 * @throws Exception 
	 */
	@Test
	public void testThermalDetectionPositive() throws Exception {
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile("src/test/resources/thermal_detection_positive_test.igc");
		
		Flight f = new FlightTestFacade(igc_points);
		
		ThermalAnalysis ta = new ThermalAnalysis();
		
		boolean got_exception = false;
		try {
			ta.analyse(f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		CirclingAnalysis ca = new CirclingAnalysis();
		ca.analyse(f);
		ta.analyse(f);
		
		assertEquals("number of thermals", 5, f.thermals.size());
		
		int[] num_circles_per_thermal = 
			{ 27, 13,  2,  8,  6 };

		int[] thermal_duration_seconds = 
			{ 780, 442, 81, 264, 157 };

		int[] thermal_average_circle_duration = 
			{ 29,  34,  41, 33, 26 };

		String[] thermal_duration_strings = 
			{ "13:00", "7:22", "1:21", "4:24", "2:37" };

		//Assert stuff about each thermal
		for (int i = 0; i < f.thermals.size(); i++) {
			Thermal t = f.thermals.get(i);
			
			assertEquals(
					"circle count in thermal #" + i, 
					num_circles_per_thermal[i], 
					t.circles.size());
			
			assertEquals(
					"thermal #"+i+" total duration seconds",
					thermal_duration_seconds[i],
					t.getTotalDurationSeconds());
			
			assertEquals(
					"thermal #"+i+" average circle duration",
					thermal_average_circle_duration[i],
					t.getAverageCircleDuration());
			
			assertEquals(
					"thermal #"+i+" total duration string",
					thermal_duration_strings[i],
					t.getTotalDuration());
		}
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		ThermalAnalysis ta = new ThermalAnalysis();
		Flight f = new FlightTestFacade(null);
		f.circles = new ArrayList<>();
		
		TestUtilities.testHasBeenRun(ta, f);
	}

}
