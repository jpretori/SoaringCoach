package soaringcoach.analysis;

import java.util.ArrayList;

import soaringcoach.Flight;
import soaringcoach.StraightPhase;

public class StraightPhaseAnalysisTestFacade extends ShortStraightPhasesAnalysis {

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
		return super.performAnalysis(flight);
	}

	@Override
	public ArrayList<StraightPhase> splitIntoSections(StraightPhase straightPhase, Flight flight)
			throws AnalysisException {
		return super.splitIntoSections(straightPhase, flight);
	}

}
