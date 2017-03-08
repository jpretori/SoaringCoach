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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.CirclingAnalysis;
import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.TestUtilities;
import soaringcoach.analysis.ThermalAnalysis;

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
