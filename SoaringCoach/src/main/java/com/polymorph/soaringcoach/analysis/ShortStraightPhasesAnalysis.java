package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.FlightAnalyser;
import com.polymorph.soaringcoach.StraightPhase;
import com.polymorph.soaringcoach.Thermal;

public class ShortStraightPhasesAnalysis extends AAnalysis {
	private static final int THRESHOLD_TIME = 10 * 1000; //10 seconds
	private static final int THRESHOLD_ANGLE = 45;
	
	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		//Add the first straight phase: takeoff roll is always straight, so we can start with the first point in the file up till the first circle
		flight.straight_phases = new ArrayList<>();
		GNSSPoint firstStraightPhaseStart = flight.igc_points.get(0);
		GNSSPoint firstStraightPhaseEnd = flight.thermals.get(0).startPoint;
		StraightPhase s = new StraightPhase(firstStraightPhaseStart, firstStraightPhaseEnd);
		flight.straight_phases.addAll(splitIntoSections(s, flight));
		
		//Add every section between two thermals
		Thermal t1 = null;
		for (Thermal t2 : flight.thermals) {
			if (t1 != null && t2 != null) {
				//last point in t1 and first point in t2, defines the boundaries of the straight section
				s = new StraightPhase(t1.endPoint, t2.startPoint);
				flight.straight_phases.addAll(splitIntoSections(s, flight));
			}
			
			t1 = t2;
		}
		
		//Add the final glide starting after the last thermal through to the last point in the flight.
		Thermal lastThermal = flight.thermals.get(flight.thermals.size() - 1);
		GNSSPoint flightLastPoint = flight.igc_points.get(flight.igc_points.size() - 1);
		s = new StraightPhase(lastThermal.endPoint, flightLastPoint);
		flight.straight_phases.addAll(splitIntoSections(s, flight));
		
		
		flight.is_short_straight_phases_analysis_complete = true;
		return flight;
	}

	/**
	 * Look through each straight phase to find the sharp turns that did not go
	 * full circle - i.e. turn points, thermal search patterns etc. Thus, any
	 * place where we turned through more than the threshold angle in less than
	 * the threshold time
	 * 
	 * @param straightPhase
	 * @return
	 * @throws AnalysisException 
	 */
	protected ArrayList<StraightPhase> splitIntoSections(StraightPhase straightPhase, Flight flight) throws AnalysisException {
		ArrayList<StraightPhase> newStraightPhasesArray = new ArrayList<>();
		StraightPhase straightPhase1 = null;
		
		int straightPhaseEndIndex = flight.igc_points.indexOf(straightPhase.end_point);
		if (straightPhaseEndIndex < 0) {
			throw new AnalysisException("Straight Phase endpoint was not found among flight's IGC points");
		}
		
		int tailIndex = flight.igc_points.indexOf(straightPhase.start_point);
		GNSSPoint pTail = flight.igc_points.get(tailIndex);
		boolean continuedTurn = false;
		for (int headIndex = tailIndex + 1; 
				headIndex <= straightPhaseEndIndex; 
				headIndex++) {
			GNSSPoint pHead = flight.igc_points.get(headIndex);
			
			long headToTailTimeDiff = pHead.data.timestamp.getTime() - pTail.data.timestamp.getTime();
			if (headToTailTimeDiff > THRESHOLD_TIME) {
				//Bring up the tail so the time difference between p1 and p2 is again near the threshold time.
				pTail = flight.igc_points.get(++tailIndex);
			
				//Only check the angle if head and tail points are far enough apart (i.e. at or near the threshold time).
				double bearingDelta = Math.abs(FlightAnalyser.calcBearingChange(pTail.bearingIntoPoint, pHead.bearingIntoPoint));
				if (bearingDelta > THRESHOLD_ANGLE) {
					if (!continuedTurn) {
						continuedTurn = true;
						straightPhase1 = new StraightPhase(straightPhase.start_point, pTail);
						newStraightPhasesArray.add(straightPhase1);
						
						// if the turn continues for several more points, this may introduce a small error. However, because
						// CirclingAnalysis is complete at this point, we can be sure that the turn does NOT go full circle, 
						// so the error will at most be a semicircle, the worst case of which is a few hundred meters.
						straightPhase = new StraightPhase(pHead, straightPhase.end_point);
					}
				} else {
					continuedTurn = false;
				}
			}
		}
		newStraightPhasesArray.add(straightPhase);
		return newStraightPhasesArray;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_short_straight_phases_analysis_complete;
	}

}
