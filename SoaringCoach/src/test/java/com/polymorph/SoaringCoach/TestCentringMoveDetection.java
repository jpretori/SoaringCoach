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
					0.00000001);
			
			assertEquals(
					circle_start_timestamp_expected[i],
					circleStart.getTimestamp(),
					0.00000001);
			
			i += 1;
		}
	}

	@Test
	public void testDetermineCircleStartHowlingGale() {
		
	}

	@Test
	public void testCorrectionDetectionDistanceChanged() {
	}

	@Test
	public void testCorrectionDetectionNoChanges() {
	}

	@Test
	public void testCorrectionDetectionBearingChanged() {
	}

	@Test
	public void testCorrectiveMoveFinalConclusionDistance() {
	}

	@Test
	public void testCorrectiveMoveFinalConclusionBearing() {
	}

	@Test
	public void testCorrectiveMoveFinalConclusionBoth() {
	}

	@Test
	public void testCorrectiveMoveFinalConclusionNeither() {
	}

	@Test
	public void testCheckTwiceRulePositive() {
	}

	@Test
	public void testCheckTwiceRuleNotApplicable() {
	}

	@Test
	public void testCheckTwiceRuleNegative() {
	}

	@Test
	public void testCheckTwiceRuleFirstCircle() {
	}

	@Test
	public void testAltitudeChange() {
	}

	@Test
	public void testClimbRatePositive() {
	}

	@Test
	public void testClimbRateNegative() {
	}
}
