package soaringcoach.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;

public class TestFlightDebriefingAnalysis {

	@Test
	public void testPerformAnalysis() throws IOException, AnalysisException {
		Flight f = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/5c6c3ke1.igc");
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		f = new ThermalAnalysis().analyse(f);
		f = new WindAnalysis().analyse(f);
		f = new CentringAnalysis().analyse(f);
		f = new StraightPhasesAnalysis().analyse(f);
		f = new DistanceAnalysis().analyse(f);
		f = new FlightDebriefingAnalysis().analyse(f);

		assertNotNull("flight debriefing variable is null", f.flightDebriefing);
		
		assertEquals(31.5, f.flightDebriefing.percentageTimeCircling, 1);
		
		assertEquals(121193.0, f.flightDebriefing.totalGroundTrackDistance, 1);
		
		assertEquals("pilot name does not match", "Kevin Mitchell", f.flightDebriefing.pilotName);
		
		assertNotNull("straight phases list is null", f.flightDebriefing.straightPhases);
		
		assertEquals("wrong number of straight phases", 50, f.flightDebriefing.straightPhases.size());
	}

}
