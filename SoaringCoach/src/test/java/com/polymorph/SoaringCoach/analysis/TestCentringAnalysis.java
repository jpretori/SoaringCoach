package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.FlightAnalyser.FlightMode;
import com.polymorph.soaringcoach.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.FlightTestFacade;
import com.polymorph.soaringcoach.Thermal;

public class TestCentringAnalysis {

	/**
	 * Positive: Correctly determine whether or not the fluctuations in bearing
	 * between a set of circles, indicate that a correction move has occurred.
	 * This is about testing the calculations that come up with the conclusion
	 * that there was a fluctuation in circle drift in the sense of a change in
	 * bearing. Also check that the drift direction and distance indication is
	 * correct.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCorrectionDetectionBearingChanged() throws Exception {
		
		PolarVector[] expect_corrections = {
				new PolarVector(0, 0),
				new PolarVector(81.6, 278), 
				new PolarVector(79.6, 295),
				new PolarVector(68.0, 194)};
		
		ArrayList<GNSSPoint> igc_points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/testCorrectionDetectionBearingChanged.igc");
		Flight f = new FlightTestFacade(igc_points);
		
		CirclingAnalysis circ_a = new CirclingAnalysis();
		f = circ_a.performAnalysis(f);
		
		Thermal t = new Thermal();
		f.thermals = new ArrayList<>(); 
		f.thermals.add(t);
		t.wind = new PolarVector(0, 0);
		for (Circle c : f.circles) {
			t.addCircle(c);
		}
		f.is_thermal_analysis_complete = true;
		f.is_wind_analysis_complete = true;
		
		CentringAnalysis cen_a = new CentringAnalysis();
		f = cen_a.performAnalysis(f);
		
		assertEquals("Number of circles", 4, f.circles.size());
		
		int i = 0;
		for (Circle circle : f.circles) {
			assertEquals(
					"Circle at index " + i, 
					expect_corrections[i].size, 
					circle.correction_vector.size,
					1);
			
			assertEquals(
					"Circle at index " + i, 
					expect_corrections[i].bearing, 
					circle.correction_vector.bearing,
					0.1);
			i += 1;
		}
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		Flight f = new FlightTestFacade(new ArrayList<>());
		AAnalysis ca = new CentringAnalysis();
		
		boolean got_exception = false;
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		got_exception = false;
		CirclingAnalysis circ_a = new CirclingAnalysis();
		f = circ_a.performAnalysis(f);
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		got_exception = false;
		ThermalAnalysis ta = new ThermalAnalysis();
		f = ta.performAnalysis(f);
		try {
			TestUtilities.testHasBeenRun(ca, f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		assertTrue(got_exception);
		
		WindAnalysis wa = new WindAnalysis();
		f = wa.performAnalysis(f);

		TestUtilities.testHasBeenRun(ca, f);
	}
	
	@Test
	public void testCalcDestinationPointNorth() {
		CentringAnalysis ca = new CentringAnalysis();
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", null, 0, 0, null, 0, 0, null);
		PolarVector wind = new PolarVector(0, 2.224);
		GNSSPoint p2 = ca.calcDestinationPoint(p1, wind, 25);
		
		assertEquals(0, p2.getLongitude(), 0.00001);
		assertEquals(0.0005, p2.getLatitude(), 0.00001);
	}
	
	@Test
	public void testCalcDestinationPointInbetweenDirection() {
		CentringAnalysis ca = new CentringAnalysis();
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", null, 1.0, 1.0, null, 0, 0, null);
		PolarVector wind = new PolarVector(85.5, 11.5);
		GNSSPoint p2 = ca.calcDestinationPoint(p1, wind, 25);
		
		assertEquals(1.00260, p2.getLongitude(), 0.0001);
		assertEquals(1.00020, p2.getLatitude(), 0.0001);
	}

	@Test
	public void testCorrectionDetectionNoChanges() throws Exception {
		Flight f = new FlightTestFacade(null);
		
		PolarVector[] centring_move_conclusion = {
				new PolarVector(0, 0),
				new PolarVector(0, 0),
				new PolarVector(268.9, 8.3),
				new PolarVector(0, 0),
				new PolarVector(0, 0)};
		
		f.circles = new ArrayList<>();
		f.thermals = new ArrayList<>();
		
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("test", df.parse("103956"), 50.76523, 3.86832, "A", 0, 0, null);
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("test", df.parse("103958"), 50.76543, 3.87226, "A", 0, 0, null);
		p2.resolve(p1);
		Circle c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		f.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("test", df.parse("104018"), 50.76585, 3.87199, "A", 0, 0, null);
		p2 = GNSSPoint.createGNSSPoint("test", df.parse("104021"), 50.76560, 3.87620, "A", 0, 0, null);
		p2.resolve(p1);
		c.setDuration(p2); //Set up previous circle duration before initialising a new Circle object
		c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		f.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("test", df.parse("104042"), 50.76598, 3.87781, "A", 0, 0, null);
		p2 = GNSSPoint.createGNSSPoint("test", df.parse("104045"), 50.76580, 3.88013, "A", 0, 0, null);
		p2.resolve(p1);
		c.setDuration(p2); //Set up previous circle duration before initialising a new Circle object
		c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		f.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("test", df.parse("104106"), 50.76616, 3.87983, "A", 0, 0, null);
		p2 = GNSSPoint.createGNSSPoint("test", df.parse("104109"), 50.76598, 3.88412, "A", 0, 0, null);
		p2.resolve(p1);
		c.setDuration(p2); //Set up previous circle duration before initialising a new Circle object
		c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		f.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("test", df.parse("104133"), 50.76642, 3.88368, "A", 0, 0, null);
		p2 = GNSSPoint.createGNSSPoint("test", df.parse("104133"), 50.76622, 3.88820, "A", 0, 0, null);
		p2.resolve(p1);
		c.setDuration(p2); //Set up previous circle duration before initialising a new Circle object
		c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		f.circles.add(c);
		
		f.is_circling_analysis_complete = true;
		
		Thermal t = new Thermal();
		t.circles = f.circles;
		t.wind = new PolarVector(85.5, 11.9);
		f.thermals.add(t);
		f.is_thermal_analysis_complete = true;
		f.is_wind_analysis_complete = true;
		
		CentringAnalysis centring = new CentringAnalysis();
		f = centring.performAnalysis(f);
		
		int i = 0;
		for (Circle circle : f.circles) {
			assertEquals(
					"Circle at index " + i, 
					centring_move_conclusion[i].bearing, 
					circle.correction_vector.bearing,
					0.1);
			
			assertEquals(
					"Circle at index " + i, 
					centring_move_conclusion[i].size, 
					circle.correction_vector.size,
					0.1);
			
			i += 1;
		}		
	}
}
