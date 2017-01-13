package com.polymorph.soaringcoach;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.beanio.BeanReader;
import org.beanio.BeanReaderException;
import org.beanio.StreamFactory;

import com.polymorph.soaringcoach.analysis.AnalysisException;
import com.polymorph.soaringcoach.analysis.CentringAnalysis;
import com.polymorph.soaringcoach.analysis.CirclingAnalysis;
import com.polymorph.soaringcoach.analysis.DistanceAnalysis;
import com.polymorph.soaringcoach.analysis.GNSSPoint;
import com.polymorph.soaringcoach.analysis.GNSSPointData;
import com.polymorph.soaringcoach.analysis.ThermalAnalysis;
import com.polymorph.soaringcoach.analysis.WindAnalysis;

public class FlightAnalyser {
	public enum FlightMode {
		TURNING_LEFT, 
		TURNING_RIGHT, 
		CRUISING
	}
	
	public ArrayList<FlightSummary> getAllFlights() {
		throw new RuntimeException("Not implemented yet");
	}
	
	public Flight getFlightDetail(FlightSummary f) {
		throw new RuntimeException("Not implemented yet");
	}
	
	/**
	 * Given an IGC file in <b>file</b>, parses the records in there and
	 * performs full analysis on the flight. Returns the resulting detailed
	 * <b>Flight</b> object.
	 * 
	 * @param file
	 * @return
	 * @throws AnalysisException
	 */
	public Flight addAndAnalyseFlight(File file) throws AnalysisException {
        ArrayList<GNSSPoint> gnss_point_list;
		try {
			gnss_point_list = readIgcFileBeanIO(file);
		} catch (IOException e) {
			throw new AnalysisException("Could not read file", e);
		}
		
		Flight f = null;
		if (gnss_point_list.size() < 1) {
			throw new AnalysisException(
					"This does not seem to be an IGC file - no valid fixes available to analyse " + file.getName());
		} else {
			f  = analyse(gnss_point_list);
		}
		
		return f;
	}

	/**
	 * Reads the given IGC file into an ArrayList of GPS fixes.
	 * 
	 * @param file
	 * @return
	 * @throws AnalysisException
	 * @throws IOException 
	 */
	private ArrayList<GNSSPoint> readIgcFileBeanIO(File file) throws AnalysisException, IOException {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(
				df.format(LocalDateTime.now()) +
				" Parsing file [" + 
				file.getName() + "]");
		
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/com/polymorph/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = null;
		ArrayList<GNSSPoint> points = new ArrayList<>();

		try {
			try {
				br = factory.createReader("igc_file", file);
				GNSSPointData pt_data = null;
				while ((pt_data = (GNSSPointData) br.read()) != null) {
					GNSSPoint pt = GNSSPoint.createGNSSPoint(pt_data);
					points.add(pt);
				}
			} catch (BeanReaderException e) {
				throw new IOException("Problem reading file " + file.getName(), e);				
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}			
		
		System.out.println(
				df.format(LocalDateTime.now()) +
				" File parsing completed");
		
		return points;
	}
	
	/**
	 * The meat & potatoes of this class - it calls all the different AAnalysis
	 * subclasses in the correct order.
	 * 
	 * @param gnssPointList
	 * @return
	 */
	private Flight analyse(ArrayList<GNSSPoint> gnssPointList) throws AnalysisException {
		Flight f = new Flight(gnssPointList);
		
		f = new DistanceAnalysis().analyse(f);
		f = new CirclingAnalysis().analyse(f);
		f = new ThermalAnalysis().analyse(f);
		f = new WindAnalysis().analyse(f);
		f = new CentringAnalysis().analyse(f);
		
		return f;
	}

	/**
	 * Helper to calculate the bearing to get from p1 to p2
	 * 
	 * @param p1
	 * @param p2
	 * @return double - bearing in degrees
	 */
	public static double calculateTrackCourse(GNSSPoint p1, GNSSPoint p2) {
		double y = Math.sin(p2.lon_radians - p1.lon_radians) * 
				Math.cos(p2.lat_radians);
		
		double x = Math.cos(p1.lat_radians) * Math.sin(p2.lat_radians) -
		        Math.sin(p1.lat_radians) * Math.cos(p2.lat_radians) *
		        Math.cos(p2.lon_radians - p1.lon_radians);
		
		double track_radians = Math.atan2(y, x);
		double track_degrees = Math.toDegrees(track_radians);
		track_degrees = (track_degrees + 360) % 360;
		return track_degrees;
	}

	/**
	 * NPE-avoiding call-through to calculate bearing from a circle's start-point to another given point.
	 * 
	 * @param c
	 * @param p2
	 * @return
	 */
	static double calculateTrackCourse(Circle c, double lat, double lon) {
		double course = -1;
		if (c != null) {
			GNSSPoint p = GNSSPoint.createGNSSPoint(null, null, lat, lon, null, 0, 0, null);
			course = calculateTrackCourse(c.getStartPoint(), p);
		}
		return course;
	}

	/**
	 * Helper to figure out the change in track course from one point's track course
	 * to the next
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double calcBearingChange(double crs1, double crs2) {
		double r = crs2 - crs1;
		
		if (Math.abs(r) >= 180) {
			if (crs2 < crs1) {
				r += 360;
			} 
			else {
				r -= 360;
			}
		}

		return r;
	}
/*
	protected void checkTwiceRule(ArrayList<Circle> circles) {
		boolean previous_circle_correction = true;
		for (Circle circle : circles) {
			if (circle.centeringCorrection()) {
				if (previous_circle_correction) {
					circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.NOT_FOLLOWED);
				} else {
					circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.FOLLOWED);
				}
			} else {
				circle.setCheckTwiceRuleIndicator(CHECK_TWICE_RULE.NOT_APPLICABLE);
			}
			previous_circle_correction = circle.centeringCorrection();
		}
	}*/
}
