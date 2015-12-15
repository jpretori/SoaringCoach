package com.polymorph.soaringcoach;

import static org.junit.Assert.*;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestGNSSPoint {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGNSSPointNE() {
		GNSSPoint pt = GNSSPoint.createGNSSPoint("testfile", "B0948523340100N01925448EA00261002670080041315512952118-0065-7300100");
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss");
		
		assertTrue(
				"Timestamp not parsed correctly, expected [094852]", 
				pt.timestamp.isEqual(LocalTime.parse("094852", formatter)));
		
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
	public void testGNSSPointSW() {
		GNSSPoint pt = GNSSPoint.createGNSSPoint("testfile", "B1648523340100S01925448WA00261002670080041315512952118-0065-7300100");
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss");
		
		assertTrue(
				"Timestamp not parsed correctly, expected [164852]", 
				pt.timestamp.isEqual(LocalTime.parse("164852", formatter)));
		
		assertEquals( 
				-33.66833333333333, 
				pt.x,
				0.0009);
		
		assertEquals(
				-19.42413333333333,
				pt.y,
				0.0009);
	}


}
