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

import soaringcoach.analysis.GNSSPoint;

public class StraightPhase implements Comparable<StraightPhase>{
	public GNSSPoint start_point;
	public GNSSPoint end_point;
	public double distance;
	
	public StraightPhase(GNSSPoint start, GNSSPoint end) {
		distance = end.distance(start);
		this.end_point = end;
		this.start_point = start;
	}

	@Override
	public int compareTo(StraightPhase o) {
		if (distance < o.distance) {
			return -1;
		}
		
		if (distance > o.distance) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StraightPhase)) {return super.equals(obj);}
		
		return new Double(distance).equals(new Double(((StraightPhase) obj).distance));
	}
	
}
