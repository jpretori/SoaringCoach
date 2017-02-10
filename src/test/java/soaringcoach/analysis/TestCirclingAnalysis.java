package soaringcoach.analysis;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.FlightAnalyserTestFacade;
import soaringcoach.FlightTestFacade;
import soaringcoach.Thermal;
import soaringcoach.FlightAnalyser.FlightMode;

public class TestCirclingAnalysis {

	
	/**
	 * This set of fixes includes: <br>
	 * - an S-turn (i.e. half a circle left, immediately followed by half a
	 * circle right)<br>
	 * - four separate full circles strung together, all in the same turn direction
	 * <br>
	 * - part of a circle at the end of the series<br>
	 * Having several circles immediately following each other, also tests the
	 * modulus arithmetic around 360/0 degrees.
	 * @throws Exception 
	 */
	@Test
	public void testCirclingAnalysisPositive() throws Exception {
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/circling_detection.igc");
		
		Flight f = new FlightTestFacade(igc_points);
		
		CirclingAnalysis ca = new CirclingAnalysis();
		f = ca.performAnalysis(f);
		
		//Check # of turns
		assertEquals("incorrect number of circles detected", 4, f.circles.size());
		
		//Check individual turn durations
		Circle t1 = f.circles.get(0);
		assertEquals("first circle duration", 32, t1.duration);
		
		Circle t2 = f.circles.get(1);
		assertEquals("second circle duration", 24, t2.duration);
		
		Circle t3 = f.circles.get(2);
		assertEquals("third circle duration", 32, t3.duration);
		
		Circle t4 = f.circles.get(3);
		assertEquals("fourth circle duration", 28, t4.duration);
	}

	/**
	 * Test with a set of points that make up an S turn, without ever quite completing a circle 
	 * @throws Exception
	 */
	@Test
	public void testCirclingDetectionDiscard() throws Exception {
		Flight f = new FlightTestFacade(new ArrayList<>());
		
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109303308755S01911128EA016190171900308"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109343308702S01911090EA016310173200309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109383308653S01911048EA016440174100308"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109423308633S01910983EA016480174500309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109463308656S01910920EA016440174200309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109503308709S01910895EA016450174400309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109543308763S01910919EA016540175200309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1109583308800S01910973EA016570175800309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110023308849S01911007EA016660176900309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110063308901S01910995EA016840178700308"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110103308929S01910941EA016900179400309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110143308914S01910879EA016920179500309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110183308865S01910851EA016910179300309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110223308810S01910877EA016860178800308"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110263308782S01910943EA016860178700307"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110303308788S01911014EA016880178900309"));
		f.igc_points.add(GNSSPoint.createGNSSPoint("testfile", "B1110343308821S01911072EA016930179500309"));		
		
		CirclingAnalysis ca = new CirclingAnalysis();
		f = ca.performAnalysis(f);
		
		assertEquals("number of turns", 0, f.circles.size());
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		CirclingAnalysis ca = new CirclingAnalysis();
		Flight f = new FlightTestFacade(new ArrayList<>());
		TestUtilities.testHasBeenRun(ca, f);
	}
	

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of circles with no wind
	 * 
	 * @throws FileNotFoundException
	 */
	public void testDetermineCircleStartNoWind() throws Exception {
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/DetermineCircleStartNoWind.igc");
		
		Flight f = new FlightTestFacade(igc_points);
		
		double[] circle_start_lat_expected = {50.76617, 50.76655};
		
		double[] circle_start_lon_expected = {3.877017, 3.877617};
		
		double[] circle_start_heading_expected = {90, 90};
		
		String[] circle_start_timestamp_expected = {"10:43:06", "10:43:29"};
		
		CirclingAnalysis ca = new CirclingAnalysis();
		ca.performAnalysis(f);
		ArrayList<Circle> circles = f.circles;
		
		assertEquals("Number of circles", 2, circles.size());
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_lat_expected[i], 
					circle.getCircleStartLatitude(), 
					0.0001);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_lon_expected[i],
					circle.getCircleStartLongitude(),
					0.0001);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_heading_expected[i],
					circle.circle_start_course,
					0.1);
			
			assertEquals(
					"Circle at index [" + i + "], timestamp [" + circle.getTimestamp() + "]", 
					circle_start_timestamp_expected[i],
					circle.getTimestamp());
			
			i += 1;
		}
	}

	@Test
	/**
	 * Accurately determine circle start lat/lon/heading/time tuple for a set of 
	 * circles with howling gale
	 * 
	 */
	public void testDetermineCircleStartHowlingGale() throws Exception {
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/DetermineCircleStartHowlingGale.igc");
		
		Flight f = new FlightTestFacade(igc_points);
		
		double[] circle_start_lat_expected = { 50.76625, 50.76707, 50.7667166666667, 50.7669 };

		double[] circle_start_lon_expected = { 3.88011, 3.88427, 3.886, 3.88817 };

		double[] circle_start_heading_expected = { 88.1, 88.1, 88.1, 88.1 };

		String[] circle_start_timestamp_expected = { "10:40:02", "10:40:17", "10:40:27", "10:40:37" };

		CirclingAnalysis ca = new CirclingAnalysis();
		ca.performAnalysis(f);
		ArrayList<Circle> circles = f.circles;
		
		assertEquals("Number of circles", 4, circles.size());
		
		int i = 0;
		for (Circle circle : circles) {
			assertEquals("Circle at index " + i, 
					circle_start_lat_expected[i], 
					circle.getCircleStartLatitude(), 
					0.0001);
			
			assertEquals("Circle at index " + i, 
					circle_start_lon_expected[i],
					circle.getCircleStartLongitude(),
					0.0001);
			
			assertEquals("Circle at index " + i, 
					circle_start_heading_expected[i],
					circle.circle_start_course,
					0.1);
			
			assertEquals("Circle at index " + i, 
					circle_start_timestamp_expected[i],
					circle.getTimestamp());
			
			i += 1;
		}
	}
	
	/**
	 * Flight contains 8 circles, first 4 are LH circles all strung together
	 * with a substantial correction between circles 2&3. The second thermal is
	 * the same except that the circles are made towards the right.
	 * 
	 * The purpose of this test is to make sure that CirclingAnalysis can deal
	 * with a mid-thermal centering move, still accepting that the elongated
	 * circle is a proper circle even though the turn rate certainly decayed
	 * below the threshold.
	 * 
	 * @throws AnalysisException
	 * @throws IOException
	 */
	@Test
	public void testCentringMoveIgnored() throws AnalysisException, IOException {
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/CenteringMoveTest.igc");
		
		Flight f = new FlightTestFacade(igc_points);
		
		f = new CirclingAnalysis().performAnalysis(f);
		f = new ThermalAnalysis().performAnalysis(f);
		
		assertEquals(2, f.thermals.size());
		Thermal t1 = f.thermals.get(0);
		Thermal t2 = f.thermals.get(1);
		
		Circle c = f.circles.get(0);
		assertEquals(21, c.duration);
		assertEquals(FlightMode.TURNING_LEFT, c.turn_direction);
		assertTrue(t1.circles.contains(c));
		
		c = f.circles.get(1);
		assertEquals(17, c.duration);
		assertEquals(FlightMode.TURNING_LEFT, c.turn_direction);
		assertTrue(t1.circles.contains(c));
		
		c = f.circles.get(2);
		assertEquals(23, c.duration);
		assertEquals(FlightMode.TURNING_LEFT, c.turn_direction);
		assertTrue(t1.circles.contains(c));
		
		c = f.circles.get(3);
		assertEquals(16, c.duration);
		assertEquals(FlightMode.TURNING_LEFT, c.turn_direction);
		assertTrue(t1.circles.contains(c));
		
		
		
		c = f.circles.get(4);
		assertEquals(21, c.duration);
		assertEquals(FlightMode.TURNING_RIGHT, c.turn_direction);
		assertTrue(t2.circles.contains(c));
		
		c = f.circles.get(5);
		assertEquals(19, c.duration);
		assertEquals(FlightMode.TURNING_RIGHT, c.turn_direction);
		assertTrue(t2.circles.contains(c));
		
		c = f.circles.get(6);
		assertEquals(23, c.duration);
		assertEquals(FlightMode.TURNING_RIGHT, c.turn_direction);
		assertTrue(t2.circles.contains(c));
		
		c = f.circles.get(7);
		assertEquals(17, c.duration);
		assertEquals(FlightMode.TURNING_RIGHT, c.turn_direction);
		assertTrue(t2.circles.contains(c));

		
		
		//Left-hand turning drift vectors
		/*
		assertEquals(9.5, f.circles.get(1).drift_vector.bearing, 3.0);
		assertEquals(8.0, f.circles.get(1).drift_vector.size, 2.0);
		
		assertEquals(221.6, f.circles.get(2).drift_vector.bearing, 3.0);
		assertEquals(79.0, f.circles.get(2).drift_vector.size, 2.0);
		
		assertEquals(342.8, f.circles.get(3).drift_vector.bearing, 3.0);
		assertEquals(11.0, f.circles.get(3).drift_vector.size, 2.0);
		*/
	}
}
