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
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.GNSSPointFacade;

public class TestGNSSPoint {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGNSSPointNE() throws ParseException {
		GNSSPoint pt = GNSSPoint.createGNSSPoint("testfile", "B0948523340100N01925448EA00261002670080041315512952118-0065-7300100");
		
		SimpleDateFormat d = new SimpleDateFormat("HHmmss");
		
		assertTrue(
				"Timestamp not parsed correctly, expected [094852]", 
				pt.data.timestamp.equals(d.parse("094852")));
		
		assertEquals( 
				33.66833333333333, 
				pt.x,
				0.0009);
		
		assertEquals(
				19.42413333333333,
				pt.y,
				0.0009);
	}
	
	@Test
	public void testGNSSPointSW() throws ParseException {
		GNSSPoint pt = GNSSPoint.createGNSSPoint("testfile", "B1648523340100S01925448WA00261002670080041315512952118-0065-7300100");
		
		SimpleDateFormat d = new SimpleDateFormat("HHmmss");

		assertTrue(
				"Timestamp not parsed correctly, expected [164852]", 
				pt.data.timestamp.equals(d.parse("164852")));
		
		assertEquals( 
				-33.66833333333333, 
				pt.x,
				0.0009);
		
		assertEquals(
				-19.42413333333333,
				pt.y,
				0.0009);
	}

	@Test
	public void testDecimalizeDegrees() {
		Double dec_deg = GNSSPointFacade.decimalizeDegrees("00", "1234", "N");
		assertEquals(0.02056667, dec_deg, 0.0000001);

		dec_deg = GNSSPointFacade.decimalizeDegrees("00", "1234", "S");
		assertEquals(-0.02056667, dec_deg, 0.0000001);
		
		dec_deg = GNSSPointFacade.decimalizeDegrees("00", "1234", "E");
		assertEquals(0.02056667, dec_deg, 0.0000001);

		dec_deg = GNSSPointFacade.decimalizeDegrees("00", "1234", "W");
		assertEquals(-0.02056667, dec_deg, 0.0000001);
		
		dec_deg = GNSSPointFacade.decimalizeDegrees("00", "0", "W");
		assertEquals(0.0, dec_deg, 0.0000001);
	}

	@Test
	public void testValidBRecord() {
		boolean is_valid_rec = GNSSPointFacade.isValidBRecord(null);
		assertEquals("null record should make invalid b record", false, is_valid_rec);
		
		is_valid_rec = GNSSPointFacade.isValidBRecord("H...");
		assertEquals("H record should make invalid b record", false, is_valid_rec);
		
		is_valid_rec =  GNSSPointFacade.isValidBRecord("B...");
		assertEquals("should be valid b record", true, is_valid_rec);
	}

	@Test
	public void testValidGPSFix() {
		GNSSPoint pt1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0, 0, "A", 0, 0, "");
		assertEquals(true, GNSSPointFacade.isValidGpsFix(pt1.data));

		GNSSPoint pt2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0, 0, "B", 0, 0, "");
		assertEquals(false, GNSSPointFacade.isValidGpsFix(pt2.data));

		GNSSPoint pt3 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0, 0, null, 0, 0, "");
		assertEquals(false, GNSSPointFacade.isValidGpsFix(pt3.data));
	}

	@Test
	public void testDistance() {
		GNSSPoint pt = GNSSPoint.createGNSSPoint("testfile", new Date(), 0, 0, "A", 0, 0, "");
		GNSSPoint pt2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.001, 0, "A", 0, 0, "");
		double dist = pt.distance(pt2);
		
		assertEquals("0.001 deg Lat at equator should be 0.06 nautical miles, i.e. 111.12 meters", 111.12, dist, 0.1);
	}

}
