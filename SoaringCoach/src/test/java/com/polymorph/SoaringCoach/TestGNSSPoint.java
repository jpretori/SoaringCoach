package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.GNSSPoint;

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
		assertEquals("Not implemented yet", 1, 2);
	}

	@Test
	public void testValidBRecord() {
		assertEquals("Not implemented yet", 1, 2);
	}

	@Test
	public void testValidGPSFix() {
		assertEquals("Not implemented yet", 1, 2);
	}

	@Test
	public void testCalcTurnRate() {
		assertEquals("Not implemented yet", 1, 2);
	}

	@Test
	public void testDistance() {
		assertEquals("Not implemented yet", 1, 2);
	}

}
