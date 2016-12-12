package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestCentringMoveDetection {

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of circles with no wind
	 * 
	 * @throws FileNotFoundException
	 */
	public void testDetermineCircleStartNoWind() throws Exception {
		Flight f = new Flight();
		
		f.igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/DetermineCircleStartNoWind.igc");
		
		double[] circle_start_lat_expected = {50.76616667, 50.7662, 50.7662, 50.7662};
		
		double[] circle_start_lon_expected = {3.877, 3.876816667, 3.876816667, 3.876816667};
		
		double[] circle_start_heading_expected = {-1, 286.0, 0.0, 0.0};
		
		String[] circle_start_timestamp_expected = {"10:43:06", "10:43:27", "10:43:46", "10:44:05"};
		
		ArrayList<Circle> circles = null;//fa.determineCircleStartValues();
		assertEquals("Number of circles", 3, circles.size());
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_lat_expected[i], 
					circle.getCircleStartLatitude(), 
					0.00000001);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_lon_expected[i],
					circle.getCircleStartLongitude(),
					0.00000001);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_heading_expected[i],
					circle.getCircleDriftBearing(),
					0.1);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_timestamp_expected[i],
					circle.getTimestamp());
			
			i += 1;
		}
	}

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of 
	 * circles with howling gale
	 * 
	 */
	public void testDetermineCircleStartHowlingGale() throws Exception {
		Flight f = new Flight();
		
		f.igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/DetermineCircleStartHowlingGale.igc");
		
		double[] circle_start_lat_expected = { 50.76625, 50.7667, 50.7667166666667, 50.7671166666667 };

		double[] circle_start_lon_expected = { 3.88011666666667, 3.88413333333333, 3.886, 3.88873333333333 };

		double[] circle_start_heading_expected = { -1, 80.0, 89.2, 77.0 };

		String[] circle_start_timestamp_expected = { "10:40:02", "10:40:16", "10:40:27", "10:40:38" };

		ArrayList<Circle> circles = null;//fa.determineCircleStartValues();
		assertEquals("Number of circles", 3, circles.size());
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals("Circle at index " + i, 
					circle_start_lat_expected[i], 
					circle.getCircleStartLatitude(), 
					0.00000001);
			
			assertEquals("Circle at index " + i, 
					circle_start_lon_expected[i],
					circle.getCircleStartLongitude(),
					0.00000001);
			
			assertEquals("Circle at index " + i, 
					circle_start_heading_expected[i],
					circle.getCircleDriftBearing(),
					0.1);
			
			assertEquals("Circle at index " + i, 
					circle_start_timestamp_expected[i],
					circle.getTimestamp());
			
			i += 1;
		}
	}

	@Test
	/**
	 * Positive: Correctly determine whether or not the fluctuations in distance
	 * between a set of circles, indicate that a correction move has occurred
	 * (only needs circle start fixes). This is about testing the calculations
	 * that come up with the conclusion that there was a fluctuation in circle
	 * drift in the sense of a change of drift rate. Also check that the
	 * drift direction and distance indication is correct.
	 */
	public void testCorrectionDetectionDistanceChanged() throws Exception {
		Flight f = new Flight();
		
		f.igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testCorrectionDetectionDistanceChanged.igc");
		
		boolean[] centring_move_conclusion = { false, false, false, true, false, false };
		
		CHECK_TWICE_RULE[] check_twice_rule_followed = {
				CHECK_TWICE_RULE.FOLLOWED, 
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE};
		
		int[] correction_vector_distance = { 13, 6, 12, 49, 14};
		
		COMPASS_POINTS[] correction_vector_direction = {
				COMPASS_POINTS.N,
				COMPASS_POINTS.N,
				COMPASS_POINTS.N,
				COMPASS_POINTS.W,
				COMPASS_POINTS.N,
				COMPASS_POINTS.N};
		
		ArrayList<Circle> circles = null;//fa.analyseCircling();
		assertEquals("Number of circles", 5, circles.size());
		
		circles = null;//fa.calculateCorrectionVectors(circles);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_rule_followed[i], 
					circle.getCheckTwiceRuleIndicator());
			
			assertEquals(
					"Circle at index " + i, 
					correction_vector_distance[i], 
					circle.correction_vector.size,
					0.1);
			
			assertEquals(
					"Circle at index " + i, 
					correction_vector_direction[i], 
					circle.correction_vector.bearing);
			i += 1;
		}
	}

	@Test
	public void testCorrectionDetectionNoChanges() throws Exception {
		Flight f = new Flight();
		
		f.igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testCorrectionDetectionNoChanges.igc");
		
		boolean[] centring_move_conclusion = { false, false, false, false, false };
		
		CHECK_TWICE_RULE[] check_twice_rule_followed = {
				CHECK_TWICE_RULE.NOT_APPLICABLE, 
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE};
		
		int[] altitude_change = { 4, -4, 0, 0, 0 };
		
		double[] climb_rate = { 0.2, -0.2, 0.0, 0.0, 0.0 };
				
		ArrayList<Circle> circles = null;//fa.analyseCircling();
		assertEquals("Number of circles", 5, circles.size());
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_rule_followed[i], 
					circle.getCheckTwiceRuleIndicator());
			
			assertEquals(
					"Circle at index " + i, 
					altitude_change[i], 
					circle.getAltitudeChange());
			
			assertEquals(
					"Circle at index " + i, 
					climb_rate[i], 
					circle.getClimbRate(), 0.1);
			i += 1;
		}		
	}


	@Test
	/**
	 * Positive: Correctly determine that the check-twice rule has been followed when it was.
	 * 
	 * @throws FileNotFoundException
	 */
	public void testCheckTwiceRulePositive() throws FileNotFoundException {
		
		boolean[] testPattern = {false, true, false, true, false};

		CHECK_TWICE_RULE[] check_twice_expected_results = {
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
		};
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		
		ArrayList<Circle> circles = null;//fa.runCheckTwiceLogic(testPattern);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_expected_results[i++], 
					circle.getCheckTwiceRuleIndicator());
		}
	}

	@Test
	/**
	 * Negative: Don't apply the check-twice rule logic when a corrective move didn't occur				
	 * @throws FileNotFoundException
	 */
	public void testCheckTwiceRuleNotApplicable() throws FileNotFoundException {
		
		boolean[] testPattern = {false, false, false};

		CHECK_TWICE_RULE[] check_twice_expected_results = {
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
		};
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		
		ArrayList<Circle> circles = null;//fa.runCheckTwiceLogic(testPattern);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_expected_results[i++], 
					circle.getCheckTwiceRuleIndicator());
		}
	}

	@Test
	/**
	 * Negative: Correctly determine that the check-twice rule was NOT followed when in fact it wasn't				
	 * @throws FileNotFoundException
	 */
	public void testCheckTwiceRuleNegative() throws FileNotFoundException {
		
		boolean[] testPattern = {false, true, true, false};

		CHECK_TWICE_RULE[] check_twice_expected_results = {
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
		};
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		
		ArrayList<Circle> circles = null;//fa.runCheckTwiceLogic(testPattern);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_expected_results[i++], 
					circle.getCheckTwiceRuleIndicator());
		}
	}

	@Test
	/**
	 * Negative: Correctly determine that the check-twice rule was NOT followed
	 * when the first circle in the thermal contains a corrective move.
	 * 
	 * @throws FileNotFoundException
	 */
	public void testCheckTwiceRuleFirstCircle() throws FileNotFoundException {
		
		boolean[] testPattern = {true, false, false};

		CHECK_TWICE_RULE[] check_twice_expected_results = {
				CHECK_TWICE_RULE.NOT_FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
		};
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		
		ArrayList<Circle> circles = null;//fa.runCheckTwiceLogic(testPattern);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_expected_results[i++], 
					circle.getCheckTwiceRuleIndicator());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testCalcDestinationPoint() {
		assertEquals("No test implemented yet", 1, 2);
	}
}
