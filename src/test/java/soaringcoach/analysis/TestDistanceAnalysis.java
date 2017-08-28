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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;

public class TestDistanceAnalysis {

	/**
	 * Test to prove removal of bug #4 (Make flight distance calculation more
	 * accurate by removing tight circles from the calculation)
	 * 
	 * @throws AnalysisException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCalcDistanceBypassingThermals() throws AnalysisException, FileNotFoundException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/distance_has_thermal.igc");
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new ThermalAnalysis().analyse(f);
		f = new StraightPhasesAnalysis().analyse(f);
		f = new DistanceAnalysis().analyse(f);
		
		assertEquals(3010, f.total_track_distance, 100);
	}
	
	@Test
	public void testCalcTotalDistanceManyPointsCloseTogether() throws AnalysisException, FileNotFoundException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/slow_movement_north.igc");
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new ThermalAnalysis().analyse(f);
		f = new StraightPhasesAnalysis().analyse(f);
		f = new DistanceAnalysis().analyse(f);

		assertEquals(206, f.total_track_distance, 1);
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		DistanceAnalysis da = new DistanceAnalysis();
		Flight flight = new FlightTestFacade(new ArrayList<GNSSPoint>());
		flight.is_distance_analysis_complete = false;
		assertFalse(da.hasBeenRun(flight ));
		flight.is_distance_analysis_complete = true;
		assertTrue(da.hasBeenRun(flight));
	}

}
