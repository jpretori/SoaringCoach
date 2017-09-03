package soaringcoach.analysis;

import soaringcoach.Flight;
import soaringcoach.FlightDebriefing;

public class FlightDebriefingAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		FlightDebriefing fd = new FlightDebriefing();
		
		fd.totalGroundTrackDistance = flight.total_track_distance;
		fd.pilotName = flight.pilot_name;
		fd.percentageTimeCircling = flight.percentageTimeCircling;
		fd.totalGroundTrackDistance = flight.total_track_distance;
		fd.straightPhases = flight.straight_phases;
		
		flight.flightDebriefing = fd;
		flight.isFlightDebriefingAnalysisComplete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.isFlightDebriefingAnalysisComplete;
	}
}
