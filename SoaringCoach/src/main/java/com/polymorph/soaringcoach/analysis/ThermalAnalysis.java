package com.polymorph.soaringcoach.analysis;

import java.util.ArrayList;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.Thermal;

/**
 * Takes the Circles and strings them together into Thermal objects. Comes up
 * with some basic stats on each Thermal. Like number of circles, thermal
 * duration, total thermal climb rate etc.
 * 
 * @author johanpretorius
 *
 */
public class ThermalAnalysis implements IAnalysis {

	@Override
	public Flight performAnalysis(Flight flight) throws AnalysisException {
		if (flight.circles == null) {
			throw new AnalysisException("Null circles array - Circling analysis has not been completed yet.");
		}
		
		flight.thermals = new ArrayList<>();
		Thermal thermal = null;
		Circle c1 = null;
		
		for (Circle c2 : flight.circles) {
			
			if (c1 != null && c2 != null) {
				
				if ((c1.timestamp.getTime() + c1.duration*1000) == c2.timestamp.getTime()) {
					//Turns are adjacent so add both to the thermal if we're starting a new record - otherwise just add c2
					
					if (thermal == null) {
						thermal = new Thermal(c1);
						flight.thermals.add(thermal);
						c1.setIncludedInThermal();
					} 
					
					thermal.addCircle(c2);
					c2.setIncludedInThermal();
				} else {
					// Set thermal=null to make sure we initialize a new thermal
					// next time two turns are adjacent 
					thermal = null;
				}
				
				//c1 and c2 were not adjacent, check if we have a 1-circle thermal
				if (!c1.isIncludedInThermal()) {
					flight.thermals.add(new Thermal(c1));
					c1.setIncludedInThermal();
				}
			}
			c1 = c2; // Switch over the pointer so we scan the list looking at two adjacent items
		}
		
		flight.is_thermal_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_thermal_analysis_complete;
	}

}
