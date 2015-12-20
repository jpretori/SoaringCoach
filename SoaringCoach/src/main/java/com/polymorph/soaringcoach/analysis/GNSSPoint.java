package com.polymorph.soaringcoach.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.vecmath.Point3d;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

/**
 * Uses the Point3d class as basis for calculations.  By convention:
 *  x = Latitude (North / South)
 *  y = Longitude (East / West)
 *  z = Altitude (in meters above mean sea level, or AMSL)
 * @author johanpretorius
 *
 */

public class GNSSPoint extends Point3d {
	public GNSSPointData data = new GNSSPointData();
	public double lat_radians = 0;
	public double lon_radians = 0;
	
	/**
	 * The track course from the previous point to this one
	 */
	public double track_course_deg = 0;
	
	/**
	 * Number of seconds since the last point
	 */
	public long seconds_since_last_fix = 0;
	
	/**
	 * Mostly, the previous point also had a track_course_deg value, meaning
	 * there could be a delta between the two, i.e. the aircraft is turning.
	 * <br>
	 * Together with how long it was since the last fix was taken, this gives a
	 * rate of turn in degrees per second.
	 */
	public double turn_rate;
	
	public String getFilename() {
		return data.getFilename();
	}
	public String getTimestamp() {
		SimpleDateFormat d = new SimpleDateFormat("HH:mm:ss");
		String time = d.format(data.timestamp);

		return time;
	}
	public double getLatitude() {
		return x;
	}
	public String getAltitudeOK() {
		return data.getAltitudeOK();
	}
	public int getPressureAltitude() {
		return data.getPressureAltitude();
	}
	public String getOther() {
		return data.getOther();
	}
	public double getLongitude() {
		return y;
	}
	public int getGnssAltitude() {
		return data.getGnssAltitude();
	}

	@Override
	public String toString() {
		return "file=[" + data.filename + "] "
				+ "timestamp=[" + getTimestamp() + "] "
				+ "lat; long=[" + getLatitude() + "; " + getLongitude() + "]";
	}
	
	/**
	 * Stub constructor
	 */
	private GNSSPoint() {}
	
	/**
	 * Create from what's loaded from the DB
	 * 
	 * @param filename
	 * @param timestamp
	 * @param latitude
	 * @param longitude
	 * @param altitude_ok
	 * @param pressure_altitude
	 * @param gnss_altitude
	 * @param other
	 * @return
	 */
	public static GNSSPoint createGNSSPoint(
			String filename, Date timestamp, double latitude, double longitude,
			String altitude_ok, int pressure_altitude, int gnss_altitude, String other) {
		GNSSPoint pt = new GNSSPoint();
		pt.data = new GNSSPointData();
		
		pt.data.setFilename(filename);
		pt.data.setTimestamp(timestamp);
		pt.x = latitude;
		pt.lat_radians = Math.toRadians(latitude);
		pt.y = longitude;
		pt.lon_radians = Math.toRadians(longitude);
		pt.data.setAltitudeOK(altitude_ok);
		pt.data.setPressureAltitude(pressure_altitude);
		pt.data.setGnssAltitude(gnss_altitude);
		pt.data.setOther(other);
		
		return pt;
	}
	
	/**
	 * Takes input as received from IGC file 
	 * (like "B0948523340100S01925448EA00261002670080041315512952118-0065-7300100")
	 * and parses it
	 * 
	 * @param file_input
	 */
	public static GNSSPoint createGNSSPoint(String filename, String file_input) {
		GNSSPointData pt_data = null;
		
		FixedFormatManager manager = new FixedFormatManagerImpl();
		pt_data = manager.load(GNSSPointData.class, file_input);
		double decimalized_lat = -1000; //init to impossible values
		double decimalized_lon = -1000;
		
		if (isValidGpsFix(file_input, pt_data)) {
			// It's a GPS fix record, so we can convert it.
			pt_data.setFilename(filename);
			
			decimalized_lat = decimalizeDegrees(pt_data.latitude_degrees, pt_data.latitude_minutes);
			if ("S".equals(pt_data.latitude_equator_ref)) {
				decimalized_lat = -decimalized_lat;
			}
			
			decimalized_lon = decimalizeDegrees(pt_data.longitude_degrees, pt_data.longitude_minutes);
			if ("W".equals(pt_data.longitude_greenwich_ref)) {
				decimalized_lon = -decimalized_lon;
			}
		} else {
			System.out.println("Discarded line: [" + file_input + "]");
		}
		
		GNSSPoint pt = GNSSPoint.createGNSSPoint(
				filename, pt_data.timestamp, decimalized_lat, decimalized_lon, pt_data.altitude_ok,
				pt_data.pressure_altitude, pt_data.gnss_altitude, pt_data.other);
		pt.data = pt_data;
		return pt;
	}

	/**
	 * Takes e.g. Latitude (as given in IGC file, i.e. 521234N for 52*1.234'N) and 
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
	private static boolean isValidGpsFix(String file_input, GNSSPointData pt) {
		//Chuck out nulls to avoid falling over ungracefully
		if (file_input == null) {
			System.err.println("File input [null]");
			return false;
		}
		
		//Is it a "Body" record, i.e. tagged as a GPS fix
		if (!"B".equals(pt.getRecordType())) {
			System.out.println("Not a 'B' record");
			return false;
		}

		//Is it marked as a "valid" altitude
		if (!"A".equals(pt.getAltitudeOK())) {
			System.out.println("Altitude validity flag not set to 'A'");
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
