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

package soaringcoach.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.vecmath.Point3d;

import soaringcoach.FlightAnalyser;
import soaringcoach.analysis.parsing.GNSSPointData;

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
	private double bearingIntoPoint = -400;
	
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
	 * Private stub constructor - force use of parameterized creation methods
	 */
	protected GNSSPoint() {}
	
	
	public static GNSSPoint createGNSSPoint(GNSSPointData pt_data) {
		GNSSPoint pt = null;
		
		double decimalized_lat = -1000; //init to impossible values
		double decimalized_lon = -1000;
	
		if (isValidGpsFix(pt_data)) {
			
			// It's a GPS fix record, so we can convert it.
			decimalized_lat = decimalizeDegrees(
					pt_data.latitude_degrees, 
					pt_data.latitude_minutes, 
					pt_data.latitudeEquatorRef);
			
			decimalized_lon = decimalizeDegrees(
					pt_data.longitude_degrees, 
					pt_data.longitude_minutes, 
					pt_data.longitude_greenwich_ref);
			
			pt = GNSSPoint.createGNSSPoint(
					null,
					pt_data.timestamp, 
					decimalized_lat, 
					decimalized_lon, 
					pt_data.altitudeOk, 
					pt_data.pressure_altitude, 
					pt_data.gnss_altitude, 
					pt_data.other);
			
			pt.data = pt_data;
			
		} else {
			SimpleDateFormat df = new SimpleDateFormat("HHmmss");
			System.out.println("Invalid GPS fix at: [" + df.format(pt_data.timestamp) + "], discarded the record");
		} 
		
		return pt;
	}
	
	/**
	 * Create from what's loaded from the DB
	 * 
	 * @param filename
	 * @param timestamp
	 * @param latitude in degrees
	 * @param longitude in degrees
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
	 * Takes e.g. Latitude (as given in IGC file, i.e. 521234N for 52*1.234'N) and 
	 * converts it to decimal format e.g. 52.10571
	 * @param degrees
	 * @param decimalized_minutes
	 * @param coordinate_ref One from the set of [N, S, E, W]
	 * @return Decimal value for latitude or long
	 */
	protected static Double decimalizeDegrees(
			String degrees, 
			String decimalized_minutes, 
			String coordinate_ref) {
		
		double x_deg = Double.parseDouble(degrees);
		double x_min = Double.parseDouble(decimalized_minutes) / 1000; //IGC format stores 1.234 as "1234"
		x_min = x_min / 60; //converts from minutes to decimal degrees
		
		double decimalized = x_deg + x_min; //adds decimal part
		
		if ("S".equals(coordinate_ref) || "W".equals(coordinate_ref)) {
			decimalized = -decimalized;
		}
		
		return decimalized;
	}

	/**
	 * Checks if a line from the file is a valid GPS fix
	 * @return
	 */
	protected static boolean isValidGpsFix(GNSSPointData pt) {
		//Is it marked as a "valid" altitude
		if (!("A".equals(pt.getAltitudeOK()) || "V".equals(pt.getAltitudeOK()))) {
			System.out.println("Altitude validity flag not set to 'A' or 'V'");
			return false;
		}
		
		return true; //All checks passed
	}
	
	/**
	 * Calculates great circle distance to another point. 
	 * @param pt2
	 * @return answer in meters
	 */
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
	
	/**
	 * Resolves several useful derived elements for this point, including the number of seconds since the last fix, 
	 * the turn rate in degrees/second and the bearing from the preceding point to this one.
	 * point.
	 * 
	 * @param p1
	 *            the preceding fix
	 */
	public void resolve(GNSSPoint p1) {
		this.seconds_since_last_fix = (this.data.timestamp.getTime() - p1.data.timestamp.getTime())/1000;
		this.bearingIntoPoint = FlightAnalyser.calculateTrackCourse(p1, this);
		
		if (p1.bearingIntoPoint > -400) { // if it's still -400, it wasn't initialised - so p1 is the first point in the file
			double track_course_delta = FlightAnalyser.calcBearingChange(p1.bearingIntoPoint, this.bearingIntoPoint);
			this.turn_rate = track_course_delta / this.seconds_since_last_fix;
		}
		
	}
	
	/**
	 * For the purposes of SoaringCoach, two <code>GNSSPoints</code> are equal iff their timestamps are equal.
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			GNSSPoint p2 = (GNSSPoint) t1;
			return this.data.timestamp.equals(p2.data.timestamp);
		} 
		catch (ClassCastException e) {return false;}
		catch (NullPointerException e) {return false;}
		
	}
	
	public double getBearingIntoPoint() {
		return this.bearingIntoPoint;
	}
}
