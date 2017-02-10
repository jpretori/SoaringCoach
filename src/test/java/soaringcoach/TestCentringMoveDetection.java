package soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.analysis.GNSSPoint;

public class TestCentringMoveDetection {



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
