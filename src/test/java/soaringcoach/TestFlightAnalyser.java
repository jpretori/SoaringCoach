package soaringcoach;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.GNSSPoint;

public class TestFlightAnalyser {

	@Test
	public void testAddAndAnalyseFlightInvalidFile() {
		FlightAnalyser fa = new FlightAnalyser();
		
		boolean got_exception = false;
		
		try {
			fa.addAndAnalyseFlight(new File("invalid.file"));
		} catch (AnalysisException e) {
			got_exception = true;
		}
		
		assertTrue(got_exception);
	}

	@Test
	public void testAddAndAnalyseFlightEmptyFile() {
		FlightAnalyser fa = new FlightAnalyser();
		
		File file = new File("src/test/resources/empty.igc");
		
		boolean got_exception = false;
		
		try {
			fa.addAndAnalyseFlight(file);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		
		assertTrue(got_exception);
	}

	@Test
	public void testAddAndAnalyseFlightValidFile() throws AnalysisException {
		FlightAnalyser fa = new FlightAnalyser();
		
		File file = new File("src/test/resources/small_valid.igc");
		
		Flight flight = fa.addAndAnalyseFlight(file);
		
		assertNotNull(flight);
		
		assertNotNull(flight.igc_points);
		
		assertTrue(flight.is_centring_analysis_complete);
		assertTrue(flight.is_circling_analysis_complete);
		assertTrue(flight.is_distance_analysis_complete);
		assertTrue(flight.is_thermal_analysis_complete);
		assertTrue(flight.is_wind_analysis_complete);
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
