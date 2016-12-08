package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.GNSSPointFacade;

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
