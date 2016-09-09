package com.polymorph.soaringcoach;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.Circle;
import com.polymorph.soaringcoach.analysis.Thermal;
import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;

public class TestFlightAnalyser {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFlightAnalyser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCalcTotalDistanceFewPointsFarApart() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0945003339755S01924837EA0450004550FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1005003359832S01917153EA0450004550Villiersdorp"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1045003402828S02028435EA0450004550FASX"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(148673, fa.calcTotalDistance(), 1);
	}

	@Test
	public void testCalcTotalDistanceFewPointsMediumDistance() {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905003339778S01924835EA0450003450FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0910003335963S01928400EA0450003450Quarry"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0915003341230S01925350EA0450003450Nekkies"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0920003343993S01921747EA0450003450Vic Peak"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0925003339778S01924835EA0450003450FAWC"));
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(36497, fa.calcTotalDistance(), 1);
	}


	@Test
	public void testCalcTotalDistanceManyPointsVeryClose() throws FileNotFoundException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/slow_movement_north.igc");
		
		FlightAnalyser fa = new FlightAnalyser(points);
		assertEquals(206, fa.calcTotalDistance(), 1);
	}
	
	/**
	 * Test with a set of points that make up an S turn, without ever quite completing a circle 
	 * @throws Exception
	 */
	@Test
	public void testCirclingDetectionDiscard() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109303308755S01911128EA016190171900308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109343308702S01911090EA016310173200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109383308653S01911048EA016440174100308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109423308633S01910983EA016480174500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109463308656S01910920EA016440174200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109503308709S01910895EA016450174400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109543308763S01910919EA016540175200309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1109583308800S01910973EA016570175800309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110023308849S01911007EA016660176900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110063308901S01910995EA016840178700308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110103308929S01910941EA016900179400309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110143308914S01910879EA016920179500309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110183308865S01910851EA016910179300309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110223308810S01910877EA016860178800308"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110263308782S01910943EA016860178700307"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110303308788S01911014EA016880178900309"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1110343308821S01911072EA016930179500309"));		
		FlightAnalyser fa = new FlightAnalyser(points);
		
		ArrayList<Circle> turns = fa.analyseCircling();
		
		assertEquals("number of turns", 0, turns.size());
	}
	
	/**
	 * This set of fixes includes: <br>
	 * - an S-turn (i.e. half a circle left, immediately followed by half a
	 * circle right)<br>
	 * - four separate full circles strung together, all in the same direction
	 * <br>
	 * - part of a circle at the end of the series<br>
	 * Having several circles immediately following each other, also tests the
	 * modulus arithmetic around 360/0 degrees.
	 * @throws Exception 
	 */
	@Test
	public void testCirclingDetectionPositive() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/circling_detection.igc");
		
		FlightAnalyser fa = new FlightAnalyser(points);
		
		ArrayList<Circle> turns = fa.analyseCircling();
		
		//Check # of turns
		assertEquals("incorrect number of turns detected", 4, turns.size());
		
		//Check individual turn durations
		Circle t1 = turns.get(0);
		assertEquals("first turn duration", 36, t1.duration);
		
		Circle t2 = turns.get(1);
		assertEquals("second turn duration", 24, t2.duration);
		
		Circle t3 = turns.get(2);
		assertEquals("third turn duration", 32, t3.duration);
		
		Circle t4 = turns.get(3);
		assertEquals("fourth turn duration", 28, t4.duration);
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
				new Circle(p1), p2.getLatitude(), p2.getLongitude()), 0.1);
		assertEquals(47.4, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p2), p3.getLatitude(), p3.getLongitude()), 0.1);
		assertEquals(90.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p3), p4.getLatitude(), p4.getLongitude()), 0.1);
		assertEquals(144.2, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p4), p5.getLatitude(), p5.getLongitude()), 0.1);
		assertEquals(180.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p5), p6.getLatitude(), p6.getLongitude()), 0.1);
		assertEquals(225.1, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p6), p7.getLatitude(), p7.getLongitude()), 0.1);
		assertEquals(270.0, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p7), p8.getLatitude(), p8.getLongitude()), 0.1);
		assertEquals(325.5, FlightAnalyserTestFacade.calculateTrackCourse(
				new Circle(p8), p9.getLatitude(), p9.getLongitude()), 0.1);
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
			{ 20, 2, 3, 10, 1, 1, 1, 1, 4, 2, 2, 1, 2 };

		int[] thermal_duration_seconds = 
			{ 537, 76, 101, 309, 42, 38, 27, 33, 135, 66, 51, 26, 51 };

		int[] thermal_average_circle_duration = 
			{ 27, 38, 34, 31, 42, 38, 27, 33, 34, 33, 26, 26, 26 };

		String[] thermal_duration_strings = 
			{ 
			"8:57", "1:16", "1:41", "5:09", "0:42", 
			"0:38", "0:27", "0:33", "2:15", "1:06", 
			"0:51", "0:26", "0:51" };

		//Assert stuff about each thermal
		for (int i = 0; i < thermals.size(); i++) {
			Thermal t = thermals.get(i);
			
			assertEquals(
					"circle count in thermal #" + i, 
					num_circles_per_thermal[i], 
					t.getTurns().size());
			
			assertEquals(
					"thermal#"+i+" total duration seconds",
					thermal_duration_seconds[i],
					t.getTotalDurationSeconds());
			
			assertEquals(
					"thermal#"+i+" average circle duration",
					thermal_average_circle_duration[i],
					t.getAverageCircleDuration());
			
			assertEquals(
					"thermal#"+i+" total duration string",
					thermal_duration_strings[i],
					t.getTotalDuration());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testCalcDestinationPoint() {
		assertEquals("No test implemented yet", 1, 2);
	}
	
	/**
	 * 
	 */
	@Test
	public void testCalculateCorrectionVectors() {
		assertEquals("No test implemented yet", 1, 2);
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testCheckTwiceRule() {
		assertEquals("No test implemented yet", 1, 2);
	}
	
	/**
	 * 
	 */
	@Test
	public void testDetectCircleCompletedCruising() {
		assertEquals("No test implemented yet", 1, 2);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testDetectCircleCompletedTurningLeftPositive() throws Exception {
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("test", null, 10, 10, null, 0, 0, null);
		p1.track_course_deg = 90;
		
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("test", null, 11, 11, null, 0, 0, null);
		p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		
		double track_course_turn_start = 90; //started circle heading east
		FlightMode mode = FlightMode.TURNING_LEFT;
		
		FlightAnalyserTestFacade fa = new FlightAnalyserTestFacade(new ArrayList<GNSSPoint>());
		
		boolean result = fa.detectCircleCompleted(p1, p2, track_course_turn_start, mode);
		
		assertEquals(true, result);
	}
	
	/**
	 * 
	 */
	@Test
	public void testDetectCircleCompletedTurningLeftNegative() {
		assertEquals("No test implemented yet", 1, 2);
	}

	/**
	 * 
	 */
	@Test
	public void testDetectCircleCompletedTurningRightPositive() {
		assertEquals("No test implemented yet", 1, 2);
	}

	/**
	 * 
	 */
	@Test
	public void testDetectCircleCompletedTurningRightNegative() {
		assertEquals("No test implemented yet", 1, 2);
	}
}
