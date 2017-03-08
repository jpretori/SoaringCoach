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

package soaringcoach;

import java.util.Date;

import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.GNSSPoint;

public class CircleTestFacade extends Circle {
	public CircleTestFacade(
			Date timestamp, 
			long duration,
			double circle_start_latitude,
			double circle_start_longitude,
			double circle_drift_bearing) {
		super(timestamp, duration, circle_start_latitude, circle_start_longitude);
	}

	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, FlightMode mode) {
		super(p1, p2, mode);
	}
	
	public CircleTestFacade(GNSSPoint p1, GNSSPoint p2, CircleTestFacade c) {
		super(p1, p2, c);
	}
	
	public boolean detectCircleCompleted(GNSSPoint p) {
		return super.detectCircleCompleted(p);
	}
	
}
