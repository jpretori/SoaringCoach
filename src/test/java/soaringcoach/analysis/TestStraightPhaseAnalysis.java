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

package soaringcoach.analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;
import soaringcoach.StraightPhase;

public class TestStraightPhaseAnalysis {

	@Test
	public void testSplitIntoSectionsDegenerate() throws IOException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/small_valid.igc").igc_points);
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(1, phases.size());
	}

	@Test
	public void testSplitIntoSectionsOnlyOne() throws IOException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/slow_movement_north.igc").igc_points);
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(1, phases.size());
	}

	@Test
	public void testSplitIntoTwoSections() throws IOException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/TwoStraightSections.igc").igc_points);
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		
		resolveAllPoints(f);
		
		//Now the test
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(2, phases.size());
	}

	@Test
	public void testSplitIntoSectionsLongSlowTurn() throws IOException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/SlowCurve.igc").igc_points);
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		
		resolveAllPoints(f);
		
		//Now the test
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(1, phases.size());
	}

	/**
	 * Utility method to resolve a dependency that all these tests have: all
	 * points must have been resolved and their "bearingIntoPoint" values
	 * calculated
	 * 
	 * @param f
	 */
	private void resolveAllPoints(FlightTestFacade f) {
		GNSSPoint p1 = null;
		for (GNSSPoint p2 : f.igc_points) {
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
			}
			p1 = p2;
		}
	}

	@Test
	public void testPerformAnalysisNoStraightSections() throws IOException, AnalysisException {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		Flight f = fa.addAndAnalyseFlight(new File("src/test/resources/DetermineCircleStartNoWind.igc"));
				
		//"Takeoff roll" and "final glide" should be all there is.
		assertEquals(2, f.straight_phases.size());
	}

}
