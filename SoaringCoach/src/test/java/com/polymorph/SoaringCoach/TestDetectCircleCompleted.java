package com.polymorph.soaringcoach;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.analysis.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.FlightAnalyser.FlightMode;

public class TestDetectCircleCompleted {

	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the West
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorLeftW() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121003337923S01931544EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121023337848S01931543EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121043337778S01931514EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121063337726S01931445EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121083337709S01931334EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121103337741S01931249EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121123337799S01931198EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121143337872S01931185EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121163337943S01931228EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121183337986S01931295EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121203337994S01931362EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121223337979S01931452EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121243337933S01931517EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121263337858S01931539EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121283337786S01931516EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121303337735S01931446EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121323337785S01931519EA0144601541testCirclingDetection"));
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_LEFT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the East
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorLeftE() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1120583337948S01931534EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121003337923S01931536EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121023337848S01931543EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121043337778S01931514EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121063337726S01931445EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121083337709S01931334EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121103337741S01931249EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121123337799S01931198EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121143337872S01931185EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121163337943S01931228EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121183337986S01931295EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121203337994S01931362EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121223337979S01931452EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121243337933S01931517EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121263337858S01931539EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121283337786S01931516EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121303337735S01931446EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121323337785S01931519EA0144601541testCirclingDetection"));
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_LEFT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates right-handed circling with the circle start heading very close to North, but to the West
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorRightW() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121003338473S01932710EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121023338415S01932709EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121043338348S01932730EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121063338287S01932815EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121083338276S01932887EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121103338288S01932984EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121123338329S01933033EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121143338388S01933067EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121163338438S01933066EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121183338492S01933038EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121203338547S01932959EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121223338559S01932877EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121243338548S01932812EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121263338512S01932754EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121283338468S01932732EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121303338418S01932714EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121323338354S01932733EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121343338291S01932813EA0144601541testCirclingDetection"));
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_RIGHT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
	
	/**
	 * Simulates left-handed circling with the circle start heading very close to North, but to the East
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testTerminatorRightE() throws Exception {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121003338473S01932705EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121023338415S01932709EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121043338348S01932730EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121063338287S01932815EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121083338276S01932887EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121103338288S01932984EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121123338329S01933033EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121143338388S01933067EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121163338438S01933066EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121183338492S01933038EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121203338547S01932959EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121223338559S01932877EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121243338548S01932812EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121263338512S01932754EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121283338468S01932732EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121303338418S01932714EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121323338354S01932733EA0144601541testCirclingDetection"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1121343338291S01932813EA0144601541testCirclingDetection"));
		
		GNSSPoint p2 = points.get(1);
		GNSSPoint p1 = points.get(0);
		p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
		p2.resolve(p1);
		
		CircleTestFacade c = null;
		p1 = null;
		p2 = null;
		int circle_count = 0;
		for (int i = 1; i < points.size(); i++) {
			p2 = points.get(i);
			
			if (p1 != null && p2 != null) {
				p2.track_course_deg = FlightAnalyserTestFacade.calculateTrackCourse(p1, p2);
				p2.resolve(p1);
				if (c == null) {
					c = new CircleTestFacade(p1, p2, FlightMode.TURNING_RIGHT);
				}
				if (c.detectCircleCompleted(p2)) { 
					circle_count++;
					c = new CircleTestFacade(p1, p2, c);
				}
			}
			
			p1 = p2;
		}
		
		assertEquals(1, circle_count);
	}
}
