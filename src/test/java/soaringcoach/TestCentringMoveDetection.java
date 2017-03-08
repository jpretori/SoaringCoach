/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

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
