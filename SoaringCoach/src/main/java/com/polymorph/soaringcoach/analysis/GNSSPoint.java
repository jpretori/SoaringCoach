package com.polymorph.soaringcoach.analysis;

import javax.vecmath.Point3d;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Uses the Point3d class as basis for calculations.  By convention:
 *  x = Latitude (North / South)
 *  y = Longitude (East / West)
 *  z = Altitude (in meters above mean sea level, or AMSL)
 * @author johanpretorius
 *
 */
public class GNSSPoint extends Point3d {
	public LocalTime timestamp = new LocalTime();
	
	/**
	 * Takes input like "B0948523340100S01925448EA00261002670080041315512952118-0065-7300100"
	 * and parses it into different fields
	 * 
	 * @param file_input
	 */
	public GNSSPoint(String file_input) {
		if (isValidGpsFix(file_input)) {
			// It's a GPS fix record, so we can convert it.
			
			//When the fix happened
			DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss");
			timestamp = LocalTime.parse(file_input.substring(1, 7), formatter);
			
			//Latitude
			String lat_deg = file_input.substring(7, 9);
			String lat_min = file_input.substring(9, 14);
			x = decimalizeDegrees(lat_deg, lat_min);
			if (file_input.substring(14, 15).equals("S")) {
				x = -x;
			}
			
			//Longitude
			String lon_deg = file_input.substring(15, 18);
			String lon_min = file_input.substring(18, 23);
			y = decimalizeDegrees(lon_deg, lon_min);
			if (file_input.substring(23, 24).equals("W")) {
				y = -y;
			}
			
			//Altitude - use GNSS altitude, we're not sure of always having pressure altitude
			String alt = file_input.substring(30, 35);
			z = Double.parseDouble(alt);
			
		} else {
			System.out.println("Discarded line: [" + file_input + "]");
		}
	}

	/**
	 * Takes e.g. Latitude (as given in IGC file, i.e. 521234 for 52*1.234') and 
	 * converts it to decimal format e.g. 52.10571
	 * @param degrees
	 * @param decimalized_minutes
	 * @return Decimal value for latitude or long
	 */
	private Double decimalizeDegrees(String degrees, String decimalized_minutes) {
		double x_deg = Double.parseDouble(degrees);
		double x_min = Double.parseDouble(decimalized_minutes) / 1000; //IGC format stores 1.234 as "1234"
		x_min = x_min / 60; //converts from minutes to decimal degrees
		return x_deg + x_min; //adds decimal part
	}

	/**
	 * Checks if a line from the file is a valid GPS fix
	 * @param file_input
	 * @return
	 */
	private boolean isValidGpsFix(String file_input) {
		//Chuck out nulls to avoid falling over ungracefully
		if (file_input == null) {
			return false;
		}
		
		//Is it a "Body" record, i.e. tagged as a GPS fix
		if (!file_input.substring(0, 1).equals("B")) {
			return false;
		}

		//Is it marked as a "valid" altitude
		if (!file_input.substring(24, 25).equals("A")) {
			return false;
		}
		
		return true; //All checks passed
	}
	
	public double distance(GNSSPoint pt2) {
		
		double lat1 = this.x * Math.PI / 180;
		double lon1 = this.y * Math.PI / 180;

		double lat2 = pt2.x * Math.PI / 180;
		double lon2 = pt2.y * Math.PI / 180;
		
		double R = 6371000; // Earth radius in metres

		double a = Math.sin((lat2-lat1)/2) * Math.sin((lat2-lat1)/2) +
		        Math.cos(lat1) * Math.cos(lat2) *
		        Math.sin((lon2-lon1)/2) * Math.sin((lon2-lon1)/2);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double d = R * c;
		
		return d;
		
		// Spherical law of cosines approximation - more performant, not as accurate
		//return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1)) * 6371000;
	}
}
