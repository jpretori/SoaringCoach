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

import soaringcoach.Flight;
import soaringcoach.StraightPhase;
import soaringcoach.Thermal;

/**
 * Works out the flight distance.
 * 
 * @author johanpretorius
 *
 */
public class DistanceAnalysis extends AAnalysis {

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		double total_dist = 0;
		
		for (StraightPhase s : flight.straight_phases) {
			total_dist += s.distance;
		}
		
		for (Thermal t : flight.thermals) {
			total_dist += t.startPoint.distance(t.endPoint);
		}
		
		flight.total_track_distance = total_dist ;
		flight.is_distance_analysis_complete = true;
		return flight;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_distance_analysis_complete;
	}

	@Override
	protected void checkPreconditions(Flight f)  throws PreconditionsFailedException {
		super.checkPreconditions(f);
		
		ThermalAnalysis thermalAnalysis = new ThermalAnalysis();
		if (!thermalAnalysis.hasBeenRun(f)) {
			throw new PreconditionsFailedException("Can not determine flight distance unless thermal analysis is complete");
		}
		
		if (!new ShortStraightPhasesAnalysis().hasBeenRun(f)) {
			throw new PreconditionsFailedException("Can not determine flight distance unless straight phase analysis is complete");
		}
	}
}
