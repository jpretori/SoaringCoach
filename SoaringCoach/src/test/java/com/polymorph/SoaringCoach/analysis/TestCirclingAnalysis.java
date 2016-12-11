package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

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
		Flight f = new Flight();
		
		f.igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/circling_detection.igc");
		
		CirclingAnalysis ca = new CirclingAnalysis();
		f = ca.performAnalysis(f);
		
		//Check # of turns
		assertEquals("incorrect number of circles detected", 4, f.circles.size());
		
		//Check individual turn durations
		Circle t1 = f.circles.get(0);
		assertEquals("first circle duration", 36, t1.duration);
		
		Circle t2 = f.circles.get(1);
		assertEquals("second circle duration", 20, t2.duration);
		
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
		Flight f = new Flight();
		f.igc_points = new ArrayList<>();
		
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
		Flight f = new Flight();
		f.igc_points = new ArrayList<>();
		TestUtilities.testHasBeenRun(ca, f);
	}

}
