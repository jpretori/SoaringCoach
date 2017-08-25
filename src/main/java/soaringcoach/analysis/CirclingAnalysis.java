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
import soaringcoach.FlightAnalyser;
import soaringcoach.FlightAnalyser.FlightMode;

/**
 * makes an array of Circles in the flight, marks each as LH or RH, calculates each’s duration, etc
 * 
 * @author johanpretorius
 *
 */
public class CirclingAnalysis extends AAnalysis {
	private static final long MAX_CENTERING_STRAIGHTEN_TIME = 10;
	// TURN_RATE_THRESHOLD is in degrees per second.  Turning faster than this constitutes making a thermal turn.
	private final int TURN_RATE_THRESHOLD = 6; 

	@Override
	protected Flight performAnalysis(Flight flight) throws AnalysisException {
		
		if (flight.igc_points == null) {
			//null points array probably means the Flight has not been initialised properly for some reason.
			throw new AnalysisException("no GPS fixes - Flight object is not initialized properly");
		}
		
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		GNSSPoint p1 = null;

		FlightMode mode = FlightMode.CRUISING;
		Circle circle = null;
		
		Circle last_halfdone_circle = null;
		GNSSPoint halfdone_circle_last_point = null;
		
		for (GNSSPoint p2 : flight.igc_points) {
			
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
				
				if (mode == FlightMode.CRUISING) {
					if (Math.abs(p2.turn_rate) > TURN_RATE_THRESHOLD) {
						mode = getTurnDirection(p2.turn_rate);
						if (detectResumingCircleAfterCentringMove(
								mode, 
								last_halfdone_circle, 
								halfdone_circle_last_point,
								p2)) {
							circle = last_halfdone_circle;
						} else {
							circle = new Circle(p1, p2, mode);
						}
					}
				} else {
					if (Math.abs(p2.turn_rate) < TURN_RATE_THRESHOLD) {
						//Turning too slowly to still call this a thermal turn
						last_halfdone_circle = circle;
						halfdone_circle_last_point = p2;
						mode = FlightMode.CRUISING;
						circle = null;
					} else if (getTurnDirection(p2.turn_rate) != mode) {
						//Switched turn direction
						mode = switchTurnDirection(mode);
						circle = new Circle(p1, p2, mode);
					} else {
						circle.detectCircleCompleted(p2);
						
						if (circle.circle_completed) {
							circle.setDuration(p2);
							circles.add(circle);
							circle = new Circle(p1, p2, circle); 
						}
					}
				}
			}
			
			//transfer the pointer so we scan through the list looking at 2 adjacent points all the time
			p1 = p2; 
		}
		
		flight.circles = circles;
		
		flight.is_circling_analysis_complete = true;
		return flight;
	}

	/**
	 * @param mode
	 * @param last_halfdone_circle
	 * @param halfdone_circle_last_point
	 * @param p2
	 * @return
	 */
	private boolean detectResumingCircleAfterCentringMove(
			FlightMode mode, 
			Circle last_halfdone_circle,
			GNSSPoint halfdone_circle_last_point, 
			GNSSPoint p2) {
		return last_halfdone_circle != null && 
				mode == last_halfdone_circle.turn_direction && 
				getElapsedTime(halfdone_circle_last_point, p2) <= MAX_CENTERING_STRAIGHTEN_TIME;
	}

	private long getElapsedTime(GNSSPoint halfdone_circle_last_point, GNSSPoint p2) {
		if (p2 != null && halfdone_circle_last_point != null) {
			return (p2.data.timestamp.getTime() - halfdone_circle_last_point.data.timestamp.getTime()) / 1000;
		} else {
			return 1000; //safest for the rest of the code
		}
	}

	private FlightMode switchTurnDirection(FlightMode mode) {
		return mode == FlightMode.TURNING_LEFT ? FlightMode.TURNING_RIGHT : FlightMode.TURNING_LEFT;
	}

	private FlightMode getTurnDirection(double turn_rate) {
		return turn_rate < 0 ? FlightMode.TURNING_LEFT : FlightMode.TURNING_RIGHT;
	}

	@Override
	public boolean hasBeenRun(Flight flight) {
		return flight.is_circling_analysis_complete;
	}

}
