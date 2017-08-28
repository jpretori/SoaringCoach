package soaringcoach.analysis;

import soaringcoach.Flight;

public class CirclingPercentageAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		// TODO Auto-generated method stub
		
		flight.is_circles_percentage_analysis_complete = true;
		return null;
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
	}

}
