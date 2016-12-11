package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;
import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class TestFlightAnalyser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFlightAnalyser() {
		//fail("Not yet implemented");
	}

	
	/**
	 * A set of points placed roughly in an octagon, with the first two points
	 * creating a bearing of due north - allowing us to test the
	 * <b>calculateTrackCourse</b> method in 8 major compass headings.
	 */
	@Test
	public void testCalculateTrackCourseTwoPoints() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", "B1106503311122S01912340EA0144601541Start");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", "B1106523311022S01912340EA0144601541N");
		GNSSPoint p3 = GNSSPoint.createGNSSPoint("testfile", "B1106543310922S01912470EA0144601541NE");
		GNSSPoint p4 = GNSSPoint.createGNSSPoint("testfile", "B1106563310922S01912570EA0144601541E");
		GNSSPoint p5 = GNSSPoint.createGNSSPoint("testfile", "B1106583311038S01912670EA0144601541SE");
		GNSSPoint p6 = GNSSPoint.createGNSSPoint("testfile", "B1107003311138S01912670EA0144601541S");
		GNSSPoint p7 = GNSSPoint.createGNSSPoint("testfile", "B1107023311238S01912550EA0144601541SW");
		GNSSPoint p8 = GNSSPoint.createGNSSPoint("testfile", "B1107043311238S01912450EA0144601541W");
		GNSSPoint p9 = GNSSPoint.createGNSSPoint("testfile", "B1107043311138S01912368EA0144601541NW");
		
		assertEquals(0.0, FlightAnalyserTestFacade.calculateTrackCourse(p1, p2), 0.1);
		assertEquals(47.4, FlightAnalyserTestFacade.calculateTrackCourse(p2, p3), 0.1);
		assertEquals(90.0, FlightAnalyserTestFacade.calculateTrackCourse(p3, p4), 0.1);
		assertEquals(144.2, FlightAnalyserTestFacade.calculateTrackCourse(p4, p5), 0.1);
		assertEquals(180.0, FlightAnalyserTestFacade.calculateTrackCourse(p5, p6), 0.1);
		assertEquals(225.1, FlightAnalyserTestFacade.calculateTrackCourse(p6, p7), 0.1);
		assertEquals(270.0, FlightAnalyserTestFacade.calculateTrackCourse(p7, p8), 0.1);
		assertEquals(325.5, FlightAnalyserTestFacade.calculateTrackCourse(p8, p9), 0.1);
	}
	
	/**
	 * A set of points placed roughly in an octagon, with the first two points
	 * creating a bearing of due north - allowing us to test the
	 * <b>calculateTrackCourse</b> method in 8 major compass headings.
	 */
	@Test
	public void testCalculateTrackCourseCircleLatLong() {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", "B1106503311122S01912340EA0144601541Start");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", "B1106523311022S01912340EA0144601541N");
		GNSSPoint p3 = GNSSPoint.createGNSSPoint("testfile", "B1106543310922S01912470EA0144601541NE");
		GNSSPoint p4 = GNSSPoint.createGNSSPoint("testfile", "B1106563310922S01912570EA0144601541E");
		GNSSPoint p5 = GNSSPoint.createGNSSPoint("testfile", "B1106583311038S01912670EA0144601541SE");
		GNSSPoint p6 = GNSSPoint.createGNSSPoint("testfile", "B1107003311138S01912670EA0144601541S");
		GNSSPoint p7 = GNSSPoint.createGNSSPoint("testfile", "B1107023311238S01912550EA0144601541SW");
		GNSSPoint p8 = GNSSPoint.createGNSSPoint("testfile", "B1107043311238S01912450EA0144601541W");
		GNSSPoint p9 = GNSSPoint.createGNSSPoint("testfile", "B1107043311138S01912368EA0144601541NW");
		
		assertEquals(0.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p1, p1, FlightMode.CRUISING), p2.getLatitude(), p2.getLongitude()), 0.1);
		assertEquals(47.4, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p2, p2, FlightMode.CRUISING), p3.getLatitude(), p3.getLongitude()), 0.1);
		assertEquals(90.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p3, p3, FlightMode.CRUISING), p4.getLatitude(), p4.getLongitude()), 0.1);
		assertEquals(144.2, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p4, p4, FlightMode.CRUISING), p5.getLatitude(), p5.getLongitude()), 0.1);
		assertEquals(180.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p5, p5, FlightMode.CRUISING), p6.getLatitude(), p6.getLongitude()), 0.1);
		assertEquals(225.1, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p6, p6, FlightMode.CRUISING), p7.getLatitude(), p7.getLongitude()), 0.1);
		assertEquals(270.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p7, p7, FlightMode.CRUISING), p8.getLatitude(), p8.getLongitude()), 0.1);
		assertEquals(325.5, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p8, p8, FlightMode.CRUISING), p9.getLatitude(), p9.getLongitude()), 0.1);
	}
	
	/**
	 * Takes a section of a real IGC file with a number of thermals in evidence,
	 * and ensures the thermals are picked up correctly.
	 * @throws Exception 
	 */
	@Test
	public void testThermalDetectionPositive() throws Exception {
		
		ArrayList<GNSSPoint> points = 
				FlightAnalyserTestFacade.loadFromFile("src/test/resources/thermal_detection_positive_test.igc");
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(points);
		
		ArrayList<Thermal> thermals = fa.calculateThermals();
		
		assertEquals("number of thermals", 13, thermals.size());
		
		int[] num_circles_per_thermal = 
			{ 20,  2,  3,   10,  1,  1,  1,  1,  4,   2,  2,  1,  2 };

		int[] thermal_duration_seconds = 
			{ 536, 74, 100, 308, 41, 36, 27, 33, 134, 65, 51, 26, 51 };

		int[] thermal_average_circle_duration = 
			{ 27,  37, 33,  31,  41, 36, 27, 33, 34,  33, 26, 26, 26 };

		String[] thermal_duration_strings = 
			{ 
			"8:56", "1:14", "1:40", "5:08", "0:41", 
			"0:36", "0:27", "0:33", "2:14", "1:05", 
			"0:51", "0:26", "0:51" };

		//Assert stuff about each thermal
		for (int i = 0; i < thermals.size(); i++) {
			Thermal t = thermals.get(i);
			
			assertEquals(
					"circle count in thermal #" + i, 
					num_circles_per_thermal[i], 
					t.getCircles().size());
			
			assertEquals(
					"thermal #"+i+" total duration seconds",
					thermal_duration_seconds[i],
					t.getTotalDurationSeconds());
			
			assertEquals(
					"thermal #"+i+" average circle duration",
					thermal_average_circle_duration[i],
					t.getAverageCircleDuration());
			
			assertEquals(
					"thermal #"+i+" total duration string",
					thermal_duration_strings[i],
					t.getTotalDuration());
		}
	}

	/**
	 * Test in 30deg increments all the way around the compass, towards the left.
	 */
	@Test
	public void testCalculateBearingChangeLeft() {
		double[] crs = {0, 330, 300, 270, 240, 210, 180, 150, 120, 90, 60, 30, 0, 330, 300};
		
		double c1 = -1;
		for (double c2 : crs) {
			if (c1 >= 0) {
				assertEquals(-30, FlightAnalyserTestFacade.calcBearingChange(c1, c2), 0.0001);
			}
			c1 = c2;
		}
	}

	/**
	 * Test in 30deg increments all the way around the compass, towards the right.
	 */
	@Test
	public void testCalculateBearingChangeRight() {
		double[] crs = {0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 0, 30, 60};
		
		double c1 = -1;
		for (double c2 : crs) {
			if (c1 >= 0) {
				assertEquals(30, FlightAnalyserTestFacade.calcBearingChange(c1, c2), 0.0001);
			}
			c1 = c2;
		}
	}
}
