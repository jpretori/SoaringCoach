package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;

public class TestCentringMoveDetection {

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of circles with no wind
	 * 
	 * @throws FileNotFoundException
	 */
	public void testDetermineCircleStartNoWind() throws FileNotFoundException {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/DetermineCircleStartNoWind.igc");
		
		double[] circle_start_lat_expected = {50.76616667, 50.7662, 50.7662, 50.7662};
		
		double[] circle_start_lon_expected = {3.877, 3.876816667, 3.876816667, 3.876816667};
		
		double[] circle_start_heading_expected = {81.8, 81.8, 81.8, 81.8};
		
		String[] circle_start_timestamp_expected = {"10:43:06", "10:43:27", "10:43:49", "10:44:11"};
		
		ArrayList<CircleStart> cs = fa.determineCircleStartValues();
		
		int i = 0;
		for (CircleStart circleStart : cs) {
			assertEquals(
					circle_start_lat_expected[i], 
					circleStart.getLatitude(), 
					0.00000001);
			
			assertEquals(
					circle_start_lon_expected[i],
					circleStart.getLongitude(),
					0.00000001);
			
			assertEquals(
					circle_start_heading_expected[i],
					circleStart.getHeading(),
					0.1);
			
			assertEquals(
					circle_start_timestamp_expected[i],
					circleStart.getTimestamp());
			
			i += 1;
		}
	}

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of 
	 * circles with howling gale
	 * 
	 */
	public void testDetermineCircleStartHowlingGale() throws FileNotFoundException {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/DetermineCircleStartHowlingGale.igc");
		
		double[] circle_start_lat_expected = { 50.76625, 50.7667, 50.7667166666667, 50.7671166666667 };

		double[] circle_start_lon_expected = { 3.88011666666667, 3.88413333333333, 3.886, 3.88873333333333 };

		double[] circle_start_heading_expected = { 80.5, 80.5, 80.5, 80.5 };

		String[] circle_start_timestamp_expected = { "10:40:02", "10:40:16", "10:40:27", "10:40:38" };

		ArrayList<CircleStart> cs = fa.determineCircleStartValues();
		
		int i = 0;
		for (CircleStart circleStart : cs) {
			assertEquals(
					circle_start_lat_expected[i], 
					circleStart.getLatitude(), 
					0.00000001);
			
			assertEquals(
					circle_start_lon_expected[i],
					circleStart.getLongitude(),
					0.00000001);
			
			assertEquals(
					circle_start_heading_expected[i],
					circleStart.getHeading(),
					0.1);
			
			assertEquals(
					circle_start_timestamp_expected[i],
					circleStart.getTimestamp());
			
			i += 1;
		}
	}

	@Test
	public void testCorrectionDetectionDistanceChanged() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectionDetectionNoChanges() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectionDetectionBearingChanged() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectiveMoveFinalConclusionDistance() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectiveMoveFinalConclusionBearing() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectiveMoveFinalConclusionBoth() {
		assertEquals(1, 2);
	}

	@Test
	public void testCorrectiveMoveFinalConclusionNeither() {
		assertEquals(1, 2);
	}

	@Test
	public void testCheckTwiceRulePositive() {
		assertEquals(1, 2);
	}

	@Test
	public void testCheckTwiceRuleNotApplicable() {
		assertEquals(1, 2);
	}

	@Test
	public void testCheckTwiceRuleNegative() {
		assertEquals(1, 2);
	}

	@Test
	public void testCheckTwiceRuleFirstCircle() {
		assertEquals(1, 2);
	}

	@Test
	public void testAltitudeChange() {
		assertEquals(1, 2);
	}

	@Test
	public void testClimbRatePositive() {
		assertEquals(1, 2);
	}

	@Test
	public void testClimbRateNegative() {
		assertEquals(1, 2);
	}
}
