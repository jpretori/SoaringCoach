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

	private int id;
	private String filename;
	private String timestamp_string;
	private String latitude;
	private String longitude;
	private String altitude_ok;
	private int pressure_altitude;
	private int gnss_altitude;
	private String other;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTimestamp() {
		return timestamp_string;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp_string = timestamp;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAltitudeOK() {
		return altitude_ok;
	}
	public void setAltitude_ok(String altitude_ok) {
		this.altitude_ok = altitude_ok;
	}
	public int getPressureAltitude() {
		return pressure_altitude;
	}
	public void setPressure_altitude(int pressure_altitude) {
		this.pressure_altitude = pressure_altitude;
	}
	public int getGnssAltitude() {
		return gnss_altitude;
	}
	public void setGnss_altitude(int gnss_altitude) {
		this.gnss_altitude = gnss_altitude;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	@Override
	public String toString() {
		return "id=[" + id + "] file=[" + filename + "] timestamp=[" + timestamp + "]";
	}
	
	/**
	 * Stub constructor
	 */
	private GNSSPoint() {}
	
	/**
	 * Takes input like "B0948523340100S01925448EA00261002670080041315512952118-0065-7300100"
	 * and parses it into different fields
	 * 
	 * @param file_input
	 */
	public static GNSSPoint createGNSSPoint(String filename, String file_input) {
		GNSSPoint pt = null;
		
		if (isValidGpsFix(file_input)) {
			// It's a GPS fix record, so we can convert it.
			
			pt  = new GNSSPoint();
			
			//When the fix happened
			DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss");
			pt.timestamp = LocalTime.parse(file_input.substring(1, 7), formatter);
			
			//Latitude
			String lat_deg = file_input.substring(7, 9);
			String lat_min = file_input.substring(9, 14);
			pt.x = decimalizeDegrees(lat_deg, lat_min);
			if (file_input.substring(14, 15).equals("S")) {
				pt.x = -pt.x;
			}
			
			//Longitude
			String lon_deg = file_input.substring(15, 18);
			String lon_min = file_input.substring(18, 23);
			pt.y = decimalizeDegrees(lon_deg, lon_min);
			if (file_input.substring(23, 24).equals("W")) {
				pt.y = -pt.y;
			}
			
			//Altitude - use GNSS altitude, we're not sure of always having pressure altitude
			String alt = file_input.substring(30, 35);
			pt.z = Double.parseDouble(alt);
			
		} else {
			System.out.println("Discarded line: [" + file_input + "]");
		}
		return pt;
	}

	/**
	 * Takes e.g. Latitude (as given in IGC file, i.e. 521234 for 52*1.234') and 
	 * converts it to decimal format e.g. 52.10571
	 * @param degrees
	 * @param decimalized_minutes
	 * @return Decimal value for latitude or long
	 */
	private static Double decimalizeDegrees(String degrees, String decimalized_minutes) {
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
	private static boolean isValidGpsFix(String file_input) {
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
		
		// Convert all from degrees to radians
		double lat1 = this.x * Math.PI / 180;
		double lon1 = this.y * Math.PI / 180;
		double lat2 = pt2.x * Math.PI / 180;
		double lon2 = pt2.y * Math.PI / 180;
		
		double earth_radius = 6371000; // Earth radius in metres

		//Haversine formula
		double lat_midpoint = (lat2-lat1)/2;
		double lon_midpoint = (lon2-lon1)/2;
		double a = Math.sin(lat_midpoint) * Math.sin(lat_midpoint) +
		        Math.cos(lat1) * Math.cos(lat2) * Math.sin(lon_midpoint) * Math.sin(lon_midpoint);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double d = earth_radius * c;
		
		return d;
		
		// Spherical law of cosines approximation - more simple, probably more performant, not as accurate
		//return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1)) * 6371000;
	}
}
