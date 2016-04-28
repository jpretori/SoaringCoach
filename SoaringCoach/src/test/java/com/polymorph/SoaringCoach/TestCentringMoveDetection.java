package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.Circle;
import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.Thermal;

public class TestCentringMoveDetection {

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of circles with no wind
	 * 
	 * @throws FileNotFoundException
	 */
	public void testDetermineCircleStartNoWind() throws Exception {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/DetermineCircleStartNoWind.igc");
		
		double[] circle_start_lat_expected = {50.76616667, 50.7662, 50.7662, 50.7662};
		
		double[] circle_start_lon_expected = {3.877, 3.876816667, 3.876816667, 3.876816667};
		
		double[] circle_start_heading_expected = {-1, 81.8, 81.8, 81.8};
		
		String[] circle_start_timestamp_expected = {"10:43:06", "10:43:27", "10:43:49", "10:44:11"};
		
		ArrayList<Circle> circles = fa.determineCircleStartValues();
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					circle_start_lat_expected[i], 
					circle.getCircleStartLatitude(), 
					0.00000001);
			
			assertEquals(
					"Circle at index " + i, 
					circle_start_lon_expected[i],
					circle.getCircleStartLongitude(),
					0.00000001);
			
			assertEquals(
					"Circle at index " + i, 
					circle_start_heading_expected[i],
					circle.getCircleDriftBearing(),
					0.1);
			
			assertEquals(
					"Circle at index " + i, 
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
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/DetermineCircleStartHowlingGale.igc");
		
		double[] circle_start_lat_expected = { 50.76625, 50.7667, 50.7667166666667, 50.7671166666667 };

		double[] circle_start_lon_expected = { 3.88011666666667, 3.88413333333333, 3.886, 3.88873333333333 };

		double[] circle_start_heading_expected = { -1, 80.0, 89.2, 77.0 };

		String[] circle_start_timestamp_expected = { "10:40:02", "10:40:16", "10:40:27", "10:40:38" };

		ArrayList<Circle> circles = fa.determineCircleStartValues();
		
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
	 * drift in the sense of a change of drift direction. Also check that the
	 * drift direction and distance indication is correct.
	 */
	public void testCorrectionDetectionDistanceChanged() throws Exception {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/CorrectionDetectionDistanceChanged.igc");
		
		boolean[] centring_move_conclusion = { false, false, false, true, false, false };
		
		CHECK_TWICE_RULE[] check_twice_rule_followed = {
				CHECK_TWICE_RULE.NOT_APPLICABLE, 
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.NOT_APPLICABLE};
		
		int[] altitude_change = { -6, -10, 20, 10, 9, 24 };
		
		double[] climb_rate = { -0.3, -0.5, 1.0, 0.5, 0.5, 1.2 };
		
		int[] circle_drift_distance = { 13, 6, 12, 49, 14, 9 };
		
		COMPASS_POINTS[] circle_drift_direction = {
				COMPASS_POINTS.SE,
				COMPASS_POINTS.SE,
				COMPASS_POINTS.E,
				COMPASS_POINTS.W,
				COMPASS_POINTS.E,
				COMPASS_POINTS.NE};
		
		ArrayList<Thermal> thermals = fa.calculateThermals();
		assertEquals(1, thermals.size());
		
		Thermal thermal = thermals.get(0);
		
		ArrayList<Circle> circles = thermal.getTurns();
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					centring_move_conclusion[i], 
					circle.centeringCorrection());

			assertEquals(
					"Circle at index " + i, 
					check_twice_rule_followed[i], 
					circle.checkTwiceRuleFollowed());
			
			assertEquals(
					"Circle at index " + i, 
					altitude_change[i], 
					circle.getAltitudeChange());
			
			assertEquals(
					"Circle at index " + i, 
					climb_rate[i], 
					circle.getClimbRate(), 0.1);
			
			assertEquals(
					"Circle at index " + i, 
					circle_drift_distance[i], 
					circle.getDriftDistance());
			
			assertEquals(
					"Circle at index " + i, 
					circle_drift_direction[i], 
					circle.getDriftDirection());
			i += 1;
		}
	}

	@Test
	public void testCorrectionDetectionNoChanges() throws Exception {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
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
				
		ArrayList<Thermal> thermals = fa.calculateThermals();
		assertEquals(1, thermals.size());
		
		Thermal thermal = thermals.get(0);
		
		ArrayList<Circle> circles = thermal.getTurns();
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					centring_move_conclusion[i], 
					circle.centeringCorrection());

			assertEquals(
					"Circle at index " + i, 
					check_twice_rule_followed[i], 
					circle.checkTwiceRuleFollowed());
			
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
	 * Positive: Correctly determine whether or not the fluctuations in bearing
	 * between a set of circles, indicate that a correction move has occurred.
	 * This is about testing the calculations that come up with the conclusion
	 * that there was a fluctuation in circle drift in the sense of a change in
	 * bearing. Also check that the drift direction and distance indication is
	 * correct.
	 * 
	 * @throws Exception
	 */
	public void testCorrectionDetectionBearingChanged() throws Exception {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(
				"src/test/resources/testCorrectionDetectionBearingChanged.igc");
		
		boolean[] centring_move_conclusion = { false, false, true, false };
		
		CHECK_TWICE_RULE[] check_twice_rule_followed = {
				CHECK_TWICE_RULE.NOT_APPLICABLE, 
				CHECK_TWICE_RULE.NOT_APPLICABLE,
				CHECK_TWICE_RULE.FOLLOWED,
				CHECK_TWICE_RULE.NOT_APPLICABLE};
		
		int[] altitude_change = { 0, 0, 0, 0 };
		
		double[] climb_rate = { 0.0, 0.0, 0.0, 0.0 };
		
		int[] circle_drift_distance = { 11, 18, 38, 13 };
		
		COMPASS_POINTS[] circle_drift_direction = {
				COMPASS_POINTS.NW,
				COMPASS_POINTS.N,
				COMPASS_POINTS.S,
				COMPASS_POINTS.NW};
		
		ArrayList<Thermal> thermals = fa.calculateThermals();
		assertEquals(1, thermals.size());
		
		Thermal thermal = thermals.get(0);
		
		ArrayList<Circle> circles = thermal.getTurns();
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					centring_move_conclusion[i], 
					circle.centeringCorrection());

			assertEquals(
					"Circle at index " + i, 
					check_twice_rule_followed[i], 
					circle.checkTwiceRuleFollowed());
			
			assertEquals(
					"Circle at index " + i, 
					altitude_change[i], 
					circle.getAltitudeChange());
			
			assertEquals(
					"Circle at index " + i, 
					climb_rate[i], 
					circle.getClimbRate(), 0.1);
			
			assertEquals(
					"Circle at index " + i, 
					circle_drift_distance[i], 
					circle.getDriftDistance());
			
			assertEquals(
					"Circle at index " + i, 
					circle_drift_direction[i], 
					circle.getDriftDirection());
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
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(new ArrayList<GNSSPoint>());
		
		ArrayList<Circle> circles = fa.runCheckTwiceLogic(testPattern);
		
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
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(new ArrayList<GNSSPoint>());
		
		ArrayList<Circle> circles = fa.runCheckTwiceLogic(testPattern);
		
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
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(new ArrayList<GNSSPoint>());
		
		ArrayList<Circle> circles = fa.runCheckTwiceLogic(testPattern);
		
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
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(new ArrayList<GNSSPoint>());
		
		ArrayList<Circle> circles = fa.runCheckTwiceLogic(testPattern);
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index " + i, 
					check_twice_expected_results[i++], 
					circle.getCheckTwiceRuleIndicator());
		}
	}
}
