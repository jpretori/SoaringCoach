package soaringcoach.analysis;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.FlightAnalyser;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;
import soaringcoach.StraightPhase;

public class TestStraightPhaseAnalysis {

	@Test
	public void testSplitIntoSectionsDegenerate() throws FileNotFoundException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/small_valid.igc"));
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(1, phases.size());
	}

	@Test
	public void testSplitIntoSectionsOnlyOne() throws FileNotFoundException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/slow_movement_north.igc"));
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(1, phases.size());
	}

	@Test
	public void testSplitIntoTwoSections() throws FileNotFoundException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/TwoStraightSections.igc"));
		
		StraightPhaseAnalysisTestFacade spa = new StraightPhaseAnalysisTestFacade();
		
		//Dependency on all points having been resolved and their "bearingIntoPoint" values calculated
		GNSSPoint p1 = null;
		for (GNSSPoint p2 : f.igc_points) {
			if (p1 != null && p2 != null) {
				p2.bearingIntoPoint = FlightAnalyser.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
			}
			p1 = p2;
		}
		
		//Now the test
		ArrayList<StraightPhase> phases = spa.splitIntoSections(
				new StraightPhase(
						f.igc_points.get(0), 
						f.igc_points.get(f.igc_points.size() - 1)), 
				f);
		
		assertEquals(2, phases.size());
	}

	@Test
	public void testSplitIntoSectionsLongSlowTurn() throws FileNotFoundException, AnalysisException {
		fail("Not yet implemented");
	}

	@Test
	public void testPerformAnalysisIntegrationTest() throws FileNotFoundException, AnalysisException {
		fail("Not yet implemented");
	}

	@Test
	public void testPerformAnalysisNoStraightSections() throws FileNotFoundException, AnalysisException {
		fail("Not yet implemented");
	}

}
