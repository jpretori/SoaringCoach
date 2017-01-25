package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.StraightPhase;
import com.polymorph.soaringcoach.Thermal;

public class ShortStraightPhasesAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		//Everything that's not a circle is a straight phase.  So make a list of all the straight phases between clumps of circles (aka Thermals)
		flight.straight_phases = new ArrayList<>();
		StraightPhase s = new StraightPhase();
		flight.straight_phases.add(s);
		
		//Add the first straight phase: takeoff roll is always straight, so we can start with the first point in the file up till the first circle
		s.start_point = flight.igc_points.get(0); 
		s.end_point = flight.circles.get(0).last_point_before_circle; 
		s.calculateDistance();
		
		Thermal t1 = null;
		for (Thermal t2 : flight.thermals) {
			if (t1 != null && t2 != null) {
				//last point in t1 and first point in t2, defines the boundaries of the straight section
				Circle t1LastCircle = t1.circles.get(t1.circles.size() - 1);
				Circle t2FirstCircle = t2.circles.get(0);
				
				s = new StraightPhase();
				s.start_point = t1LastCircle.last_point_in_circle;
				s.end_point = t2FirstCircle.last_point_before_circle;
				s.calculateDistance();
				flight.straight_phases.add(s);
			}
			
			t1 = t2;
		}
		
		//Now we have all the straight phases up till the start of the last thermal.  Add the final glide through to the end of the landing roll.
		s = new StraightPhase();
		Thermal lastThermal = flight.thermals.get(flight.thermals.size() - 1);
		s.start_point = lastThermal.circles.get(lastThermal.circles.size() - 1).last_point_in_circle;
		s.end_point = flight.igc_points.get(flight.igc_points.size() - 1);
		s.calculateDistance();
		flight.straight_phases.add(s);
		
		flight.is_short_straight_phases_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_short_straight_phases_analysis_complete;
	}

}
