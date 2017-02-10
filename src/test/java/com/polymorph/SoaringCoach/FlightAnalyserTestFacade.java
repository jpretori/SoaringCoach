package com.polymorph.soaringcoach;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.polymorph.soaringcoach.Circle;
import com.polymorph.soaringcoach.FlightAnalyser;
import com.polymorph.soaringcoach.analysis.GNSSPoint;

public class FlightAnalyserTestFacade extends FlightAnalyser {
	
	public static double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		return FlightAnalyser.calculateTrackCourse(p1, p2);
	}
	
	public static double calculateTrackCourse(Circle c, double lat, double lon) {
		return FlightAnalyser.calculateTrackCourse(c, lat, lon);
	}
	
	public static double calcBearingChange(double crs1, double crs2) {
		return FlightAnalyser.calcBearingChange(crs1, crs2);
	}
	
	public static ArrayList<GNSSPoint> loadFromFile(String filename) throws FileNotFoundException {
		ArrayList<GNSSPoint> igc_points = new ArrayList<>();
		
		for (Scanner sc = new Scanner(new File(filename)); sc.hasNext();) {
			String line = sc.nextLine();
			
			GNSSPoint pt = GNSSPoint.createGNSSPoint(filename, line);
			if (pt != null) {
				igc_points.add(pt);
			}
		}
		
		return igc_points;
	}

	public ArrayList<Circle> determineCircleStartValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
