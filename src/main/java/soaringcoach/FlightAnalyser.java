/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *   SoaringCoach is a tool for analysing IGC files produced by modern FAI
 *   flight recorder devices, and providing the pilot with useful feedback
 *   on how effectively they are flying.    
 *   Copyright (C) 2017 Johan Pretorius
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   The author can be contacted via email at pretoriusjf@gmail.com, or 
 *   by paper mail by addressing as follows: 
 *      Johan Pretorius 
 *      PO Box 990 
 *      Durbanville 
 *      Cape Town 
 *      7551
 *      South Africa
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package soaringcoach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.beanio.BeanReader;
import org.beanio.BeanReaderException;
import org.beanio.StreamFactory;

import soaringcoach.analysis.AnalysisException;
import soaringcoach.analysis.CentringAnalysis;
import soaringcoach.analysis.CirclingAnalysis;
import soaringcoach.analysis.DistanceAnalysis;
import soaringcoach.analysis.FlightSummaryAnalysis;
import soaringcoach.analysis.GNSSPoint;
import soaringcoach.analysis.ShortStraightPhasesAnalysis;
import soaringcoach.analysis.ThermalAnalysis;
import soaringcoach.analysis.WindAnalysis;
import soaringcoach.analysis.parsing.GNSSPointData;
import soaringcoach.analysis.parsing.PICName;

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
	 * 
	 * @param igc_input
	 * @return
	 * @throws AnalysisException 
	 */
	public Flight addAndAnalyseFlight(InputStream igc_input) throws AnalysisException {
    	InputStreamReader isr = new InputStreamReader(igc_input);
    	BufferedReader igc_reader = new BufferedReader(isr);
    	
    	Flight flight = new Flight();
    	
    	try {
    		flight = readIgcFileBeanIO(igc_reader, flight);
    	} catch (IOException e) {
    		throw new AnalysisException("Could not read IGC content");
    	}
		
		if (flight.igc_points.size() < 1) {
			throw new AnalysisException("This does not seem to be an IGC file - no valid fixes available to analyse");
		} else {
			flight = analyse(flight);
		}

    	return flight;
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
        Flight flight = new Flight();
        
		try {
			FileReader fileReader = new FileReader(file);
			flight = readIgcFileBeanIO(fileReader, flight);
		} catch (IOException e) {
			throw new AnalysisException("Could not read file " + file.getName(), e);
		}
		
		if (flight.igc_points.size() < 1) {
			throw new AnalysisException(
					"This does not seem to be an IGC file - no valid fixes available to analyse " + file.getName());
		} else {
			flight = analyse(flight);
		}
		
		return flight;
	}

	/**
	 * Reads the given IGC file into an ArrayList of GPS fixes.
	 * 
	 * @param file
	 * @return
	 * @throws AnalysisException
	 * @throws IOException 
	 */
	private Flight readIgcFileBeanIO(Reader file, Flight f) throws AnalysisException, IOException {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(LocalDateTime.now()) + " Parsing IGC");
		
		StreamFactory factory = StreamFactory.newInstance();
		
		factory.load("src/main/java/soaringcoach/analysis/igc_mapping.xml");

		BeanReader br = null;
		f.igc_points = new ArrayList<>();

		try {
			try {
				br = factory.createReader("igc_file", file);
				GNSSPointData pt_data = null;
				Object bean = null;
				
				while ((bean = br.read()) != null) {
					if (bean instanceof GNSSPointData) {
						pt_data = (GNSSPointData) bean;
						
						GNSSPoint pt = GNSSPoint.createGNSSPoint(pt_data);
						if (pt != null) {
							f.igc_points.add(pt);
						}
					} else if (bean instanceof PICName) {
						f.pilot_name = ((PICName) bean).picName;
					}
				}
			} catch (BeanReaderException e) {
				throw new IOException("Problem reading IGC Data", e);				
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}			
		
		System.out.println(
				df.format(LocalDateTime.now()) +
				" File parsing completed");
		
		return f;
	}
	
	/**
	 * The meat & potatoes of this class - it calls all the different AAnalysis
	 * subclasses in the correct order.
	 * 
	 * @param gnssPointList
	 * @return
	 */
	private Flight analyse(Flight f) throws AnalysisException {
		f = new DistanceAnalysis().analyse(f);
		f = new CirclingAnalysis().analyse(f);
		f = new ThermalAnalysis().analyse(f);
		f = new WindAnalysis().analyse(f);
		f = new CentringAnalysis().analyse(f);
		f = new ShortStraightPhasesAnalysis().analyse(f);
		f = new FlightSummaryAnalysis().analyse(f);
		
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
