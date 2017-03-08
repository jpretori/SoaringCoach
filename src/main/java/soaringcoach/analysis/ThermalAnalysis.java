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
	protected Flight performAnalysis(Flight flight) {	
		flight.thermals = new ArrayList<>();
		
		Thermal thermal = new Thermal();
		for (Circle c : flight.circles) {
			if (!thermal.addCircle(c)) {
				flight.thermals.add(thermal);
				thermal = new Thermal(c);
			}
		}
		
		//For loop never adds the last thermal it finds, so do it "manually"
		if (!thermal.circles.isEmpty()) {
			flight.thermals.add(thermal);
		}
		
		flight.is_thermal_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_thermal_analysis_complete;
	}
	
	@Override
	protected void checkPreconditions(Flight flight) throws PreconditionsFailedException {
		super.checkPreconditions(flight);
		
		if (flight.circles == null) {
			throw new PreconditionsFailedException("Null circles array - Circling analysis has not been completed yet.");
		}
		
		
	}
}
