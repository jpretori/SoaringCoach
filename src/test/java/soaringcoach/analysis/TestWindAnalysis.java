package soaringcoach.analysis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import soaringcoach.Circle;
import soaringcoach.Flight;
import soaringcoach.FlightTestFacade;
import soaringcoach.Thermal;
import soaringcoach.FlightAnalyser.FlightMode;
import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.WindAnalysis;

public class TestWindAnalysis {
	public WindAnalysis wa = new WindAnalysis();

	@Test
	public void testPerformAnalysisNullThermals() {
		Flight f = new FlightTestFacade(null);
		f.thermals = null;
		
		boolean got_exception = false;
		try {
			wa.performAnalysis(f);
		} catch (AnalysisException e) {
			got_exception = true;
		}
		
		assertEquals(true, got_exception);
	}

	@Test
	public void testPerformAnalysisPerfectCircles() throws AnalysisException {
		Thermal t = new Thermal();
		t.wind = null;
		t.circles = new ArrayList<>();
		
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0, "A", 500, 500, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0001, "A", 500, 500, "");;
		p2.resolve(p1);
		Circle c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.001, 0.001, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.001, 0.0011, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.002, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.0021, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.003, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.0031, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		Flight f = new FlightTestFacade(null);
		f.thermals = new ArrayList<>();
		f.thermals.add(t);
		
		f = wa.performAnalysis(f);
		
		assertEquals(1, f.thermals.size());
		
		Thermal t_test = f.thermals.get(0);
		
		assertNotNull(t.wind);
		
		assertEquals(45.0, t_test.wind.bearing, 0.1);
		assertEquals(15.73, t_test.wind.size, 0.1);
		
		Circle c1, c2, c3, c4;
		c1 = t_test.circles.get(0);
		c2 = t_test.circles.get(1);
		c3 = t_test.circles.get(2);
		c4 = t_test.circles.get(3);
		
		assertNotNull(c1.drift_vector);
		assertNotNull(c2.drift_vector);
		assertNotNull(c3.drift_vector);
		assertNotNull(c4.drift_vector);
		
		assertEquals(0, c1.drift_vector.bearing, 1);
		assertEquals(0, c1.drift_vector.size, 1);
		
		assertEquals(45.0, c2.drift_vector.bearing, 1);
		assertEquals(157.3, c2.drift_vector.size, 1);
		
		assertEquals(45.0, c3.drift_vector.bearing, 1);
		assertEquals(157.3, c3.drift_vector.size, 1);
		
		assertEquals(45.0, c4.drift_vector.bearing, 1);
		assertEquals(157.3, c4.drift_vector.size, 1);
	}

	@Test
	public void testPerformAnalysisSlightlyMovingCircles() throws AnalysisException {
		Thermal t = new Thermal();
		t.wind = null;
		t.circles = new ArrayList<>();
		
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0, "A", 500, 500, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0001, "A", 500, 500, "");;
		p2.resolve(p1);
		Circle c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.00101, 0.001, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.00101, 0.0011, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.002, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.0021, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.003, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.0031, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		Flight f = new FlightTestFacade(null);
		f.thermals = new ArrayList<>();
		f.thermals.add(t);
		
		f = wa.performAnalysis(f);
		
		assertEquals(1, f.thermals.size());
		
		t = f.thermals.get(0);
		
		assertNotNull(t.wind);
		
		assertEquals(45.0, t.wind.bearing, 0.1);
		assertEquals(15.73, t.wind.size, 0.1);
		
		assertFalse(t.could_not_calculate_wind);
		
		Circle c1, c2, c3;
		c1 = t.circles.get(0);
		c2 = t.circles.get(1);
		c3 = t.circles.get(2);
		
		assertNotNull(c1.drift_vector);
		assertNotNull(c2.drift_vector);
		assertNotNull(c3.drift_vector);
		
		assertEquals(44.715, c2.drift_vector.bearing, 1);
		assertEquals(158.0, c2.drift_vector.size, 1);
		
		assertEquals(45.288, c3.drift_vector.bearing, 1);
		assertEquals(156.5, c3.drift_vector.size, 1);
	}

	@Test
	public void testPerformAnalysisWildlyMovingCircles() throws AnalysisException {
		Thermal t = new Thermal();
		t.wind = null;
		t.circles = new ArrayList<>();
		
		GNSSPoint p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0, "A", 500, 500, "");
		GNSSPoint p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0, 0.0001, "A", 500, 500, "");;
		p2.resolve(p1);
		Circle c = new Circle(p1, p2, FlightMode.TURNING_LEFT);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0015, 0.001, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.0015, 0.0011, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.002, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.002, 0.0021, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.003, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.003, 0.0031, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.004, 0.004, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.004, 0.0041, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);
		
		p1 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.005, 0.005, "A", 500, 500, "");
		p2 = GNSSPoint.createGNSSPoint("testfile", new Date(), 0.005, 0.0051, "A", 500, 500, "");;
		p2.resolve(p1);
		c = new Circle(p1, p2, c);
		c.duration = 10;
		t.circles.add(c);

		Flight f = new FlightTestFacade(null);
		f.thermals = new ArrayList<>();
		f.thermals.add(t);
		
		f = wa.performAnalysis(f);
		
		assertEquals(1, f.thermals.size());
		
		t = f.thermals.get(0);
		
		assertFalse(t.could_not_calculate_wind);
		assertNotNull(t.wind);
		assertEquals(45.0, t.wind.bearing, 0.1);
		assertEquals(157.2/t.getAverageCircleDuration(), t.wind.size, 0.1);
		
		Circle c1, c2, c3;
		c1 = t.circles.get(0);
		c2 = t.circles.get(1);
		c3 = t.circles.get(2);
		
		assertNotNull(c1.drift_vector);
		assertNotNull(c2.drift_vector);
		assertNotNull(c3.drift_vector);
		
		assertEquals(33.69, c2.drift_vector.bearing, 0.1);
		assertEquals(200.5, c2.drift_vector.size, 0.1);
		
		assertEquals(63.435, c3.drift_vector.bearing, 0.1);
		assertEquals(124.3, c3.drift_vector.size, 0.1);
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		Flight f = new FlightTestFacade(null);
		f.thermals = new ArrayList<>();
		TestUtilities.testHasBeenRun(wa, f);
	}

}
