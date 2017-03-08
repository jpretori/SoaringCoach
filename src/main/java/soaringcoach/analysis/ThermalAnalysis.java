/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach.analysis;

import java.util.ArrayList;

import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.Thermal;

/**
 * Takes the Circles and strings them together into Thermal objects. Comes up
 * with some basic stats on each Thermal. Like number of circles, thermal
 * duration, total thermal climb rate etc.
 * 
 * @author johanpretorius
 *
 */
public class ThermalAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		if (flight.circles == null) {
			throw new AnalysisException("Null circles array - Circling analysis has not been completed yet.");
		}
		
		flight.thermals = new ArrayList<>();
		Thermal thermal = null;
		Circle c1 = null;
		
		for (Circle c2 : flight.circles) {
			
			if (c1 != null && c2 != null) {
				
				thermal = addCirclesToThermal(thermal, c1, c2);
				if (thermal != null) {
					flight.thermals.add(thermal);
				}

				//if c1 isn't in a thermal yet, it means it's a 1-circle thermal.  Add it into it's own Thermal object.
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

	/**
	 * Figures out whether two circles are adjacent, adding one or both as
	 * appropriate (<code>c1</code> may already be in there). Returns null if
	 * the circles are not adjacent - this this does not work for single-circle
	 * "thermals".
	 * 
	 * @param thermal
	 * @param c1
	 * @param c2
	 * @return
	 */
	private Thermal addCirclesToThermal(Thermal thermal, Circle c1, Circle c2) {
		if ((c1.timestamp.getTime() + c1.duration*1000) == c2.timestamp.getTime()) {
			//Turns are adjacent so add both to the thermal if we're starting a new record - otherwise just add c2
			
			if (thermal == null) {
				thermal = new Thermal(c1);
				c1.setIncludedInThermal();
			} 
			
			thermal.addCircle(c2);
			c2.setIncludedInThermal();
		} else {
			// Set thermal=null to make sure we initialize a new thermal
			// next time two turns are adjacent 
			thermal = null;
		}
		return thermal;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_thermal_analysis_complete;
	}

}
