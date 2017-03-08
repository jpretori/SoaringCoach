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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import soaringcoach.analysis.GNSSPoint;

public class TestCalculateTrackCourse {

	@Test
	public void testNNE() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1, 1, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 2, 1.01, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(0.5725, crs, 0.001);
	}

	@Test
	public void testNNW() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1, 1, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 2, 0.99, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(359.4272, crs, 0.001);
	}

	@Test
	public void testNE() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1.00, 1.00, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 1.01, 1.01, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(44.996, crs, 0.001);
	}

	@Test
	public void testN() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("", null, 1.00, 1.00, "A", 0, 0, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("", null, 1.01, 1.00, "A", 0, 0, "");
		
		double crs = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		assertEquals(0.000, crs, 0.001);
	}
}
