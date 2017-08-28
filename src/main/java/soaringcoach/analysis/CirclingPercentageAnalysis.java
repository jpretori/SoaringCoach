package soaringcoach.analysis;

import java.util.Date;

import soaringcoach.Circle;
import soaringcoach.Flight;

public class CirclingPercentageAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		double flightDuration = 0.0;
		Date lastPointTime = flight.igc_points.get(flight.igc_points.size() - 1).data.getTimestamp();
		Date firstPointTime = flight.igc_points.get(0).data.getTimestamp();
		flightDuration = lastPointTime.getTime() - firstPointTime.getTime();
		flightDuration /= 1000;
		
		double circlingDuration = 0.0;
		for (Circle c : flight.circles) {
			circlingDuration += c.duration;
		}
		
		flight.percentageTimeCircling = 100 * (circlingDuration / flightDuration);
		
		flight.is_circles_percentage_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_circles_percentage_analysis_complete;
	}
	
	@Override
	protected void checkPreconditions(Flight f) throws PreconditionsFailedException {
		if (!f.is_circles_analysis_complete) {
			throw new PreconditionsFailedException(
					"Cannot calculate percentage of time spent circling until circles analysis has been completed");
		}
		
		if (f.igc_points.isEmpty()) {
			throw new PreconditionsFailedException("No GPS fixes, cannot calculate circling percentage");
		}
	}

}
