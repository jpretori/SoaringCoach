package soaringcoach.analysis;

import soaringcoach.Flight;
import soaringcoach.FlightSummary;

public class FlightSummaryAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		FlightSummary fs = new FlightSummary();
		
		fs.id = flight.id;
		fs.total_track_distance = flight.total_track_distance;
		fs.pilotName = flight.pilot_name;
		
		flight.flightSummary = fs;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.isFlightSummaryAnalysisComplete;
	}
}
