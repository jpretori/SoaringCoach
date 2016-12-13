package com.polymorph.soaringcoach.analysis;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import com.polymorph.soaringcoach.Flight;
import com.polymorph.soaringcoach.FlightAnalyserTestFacade;
import com.polymorph.soaringcoach.FlightTestFacade;

public class TestDistanceAnalysis {

	@Test
	public void testCalcTotalDistanceManyPointsCloseTogether() throws AnalysisException, FileNotFoundException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points = FlightAnalyserTestFacade.loadFromFile(
				"src/test/resources/slow_movement_north.igc");
		
		Flight f = new FlightTestFacade(points);
		
		DistanceAnalysis da = new DistanceAnalysis();
		
		da.performAnalysis(f);
		
		assertEquals(206, f.total_track_distance, 1);
	}

	@Test
	public void testCalcTotalDistanceFewPointsFarApart() throws AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0945003339755S01924837EA0450004550FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1005003359832S01917153EA0450004550Villiersdorp"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B1045003402828S02028435EA0450004550FASX"));
		
		Flight f = new FlightTestFacade(points);
		
		DistanceAnalysis da = new DistanceAnalysis();
		
		da.performAnalysis(f);
		
		assertEquals(148673, f.total_track_distance, 1);
	}

	@Test
	public void testCalcTotalDistanceFewPointsMediumDistance() throws AnalysisException {
		ArrayList<GNSSPoint> points = new ArrayList<>();
		
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0905003339778S01924835EA0450003450FAWC"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0910003335963S01928400EA0450003450Quarry"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0915003341230S01925350EA0450003450Nekkies"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0920003343993S01921747EA0450003450Vic Peak"));
		points.add(GNSSPoint.createGNSSPoint("testfile", "B0925003339778S01924835EA0450003450FAWC"));
		
		Flight f = new FlightTestFacade(points);
		
		DistanceAnalysis da = new DistanceAnalysis();
		
		da.performAnalysis(f);
		
		assertEquals(36497, f.total_track_distance, 1);
	}

	@Test
	public void testPerformAnalysisNullPoints() throws AnalysisException {
		Flight f = new FlightTestFacade(null);
		DistanceAnalysis da = new DistanceAnalysis();
		
		da.performAnalysis(f);
		
		assertEquals(0, f.total_track_distance, 0.001);
		
	}

	@Test
	public void testHasBeenRun() throws AnalysisException {
		DistanceAnalysis da = new DistanceAnalysis();
		TestUtilities.testHasBeenRun(da, null);
	}

}
