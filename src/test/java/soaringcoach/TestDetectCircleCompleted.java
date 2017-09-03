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

import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.GNSSPoint;

public class TestDetectCircleCompleted {

	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the West
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorLeftW() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testTerminatorLeftW.igc").igc_points;
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_LEFT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the East
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorLeftE() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testTerminatorLeftE.igc").igc_points;
				
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_LEFT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates right-handed circling with the circle start heading very close to North, but to the West
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorRightW() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testTerminatorRightW.igc").igc_points;
				
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_RIGHT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the East
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorRightE() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testTerminatorRightE.igc").igc_points;
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_RIGHT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
}
