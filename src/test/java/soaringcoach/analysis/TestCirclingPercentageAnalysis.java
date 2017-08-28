package soaringcoach.analysis;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;

public class TestCirclingPercentageAnalysis {

	@Test
	public void testCirclingPercentageHigh() throws FileNotFoundException, AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentageHigh.igc");
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(73.0, f.percentageTimeCircling, 5.0);
	}

	@Test
	public void testCirclingPercentageNone() throws FileNotFoundException, AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentage_none.igc");
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(0.0, f.percentageTimeCircling, 0.01);
	}

	@Test
	public void testCirclingPercentageLow() throws FileNotFoundException, AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CirclingPercentageLow.igc");
		
		Flight f = new FlightTestFacade(points);
		
		f = new CirclesAnalysis().analyse(f);
		f = new CirclingPercentageAnalysis().analyse(f);
		
		assertEquals(9.0, f.percentageTimeCircling, 5.0);
	}
}
