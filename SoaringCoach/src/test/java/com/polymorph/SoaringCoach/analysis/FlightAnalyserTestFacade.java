package com.polymorph.soaringcoach.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.polymorph.soaringcoach.CircleStart;

public class FlightAnalyserTestFacade extends FlightAnalyser {
	
	public FlightAnalyserTestFacade(ArrayList<GNSSPoint> file) {
		super(file);
	}

	public double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		return super.calculateTrackCourse(p1, p2);
	}
	
	public FlightAnalyserTestFacade(String filename) throws FileNotFoundException  {
		super(null);
		
		ArrayList<GNSSPoint> igc_points = new ArrayList<>();
		
		for (Scanner sc = new Scanner(new File(filename)); sc.hasNext();) {
			String line = sc.nextLine();
			igc_points.add(GNSSPoint.createGNSSPoint(filename, line));
		}
		
		super.setIgcPoints(igc_points);
	}

	public ArrayList<Circle> runCheckTwiceLogic(boolean[] testPattern) {
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		for (boolean b : testPattern) {
			Circle c = new Circle(null, 20);
			c.setCorrectiveMoveDetected(b);
			circles.add(c);
		}
		
		super.checkTwiceRule(circles);
		
		return circles;
	}

	public ArrayList<CircleStart> determineCircleStartValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<GNSSPoint> loadFromFile(String filename) throws FileNotFoundException {
		ArrayList<GNSSPoint> igc_points = new ArrayList<>();
		
		for (Scanner sc = new Scanner(new File(filename)); sc.hasNext();) {
			String line = sc.nextLine();
			igc_points.add(GNSSPoint.createGNSSPoint(filename, line));
		}
		
		return igc_points;
	}
}
