package soaringcoach.analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Flight;
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
	public void testSplitIntoSectionsLongSlowTurn() throws FileNotFoundException, AnalysisException {
		FlightTestFacade f = new FlightTestFacade(
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/SlowCurve.igc"));
		
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
				p2.bearingIntoPoint = FlightAnalyser.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
			}
			p1 = p2;
		}
	}

	@Test
	public void testPerformAnalysisNoStraightSections() throws FileNotFoundException, AnalysisException {
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade();
		Flight f = fa.addAndAnalyseFlight(new File("src/test/resources/DetermineCircleStartNoWind.igc"));
				
		//"Takeoff roll" and "final glide" should be all there is.
		assertEquals(2, f.straight_phases.size());
	}

}
