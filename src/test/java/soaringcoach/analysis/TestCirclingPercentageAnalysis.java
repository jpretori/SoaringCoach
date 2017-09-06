package soaringcoach.analysis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;

public class TestCirclingPercentageAnalysis {

	@Test
	public void testCirclingPercentageHigh() throws AnalysisException, IOException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentageHigh.igc").igc_points;
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(63.0, f.percentageTimeCircling, 5.0);
	}

	@Test
	public void testCirclingPercentageNone() throws IOException, AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentage_none.igc").igc_points;
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(0.0, f.percentageTimeCircling, 0.01);
	}

	@Test
	public void testCirclingPercentageLow() throws IOException, AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentageLow.igc").igc_points;
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(9.0, f.percentageTimeCircling, 5.0);
	}
}
