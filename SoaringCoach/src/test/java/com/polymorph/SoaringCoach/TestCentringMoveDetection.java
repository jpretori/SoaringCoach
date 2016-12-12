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
	
}
