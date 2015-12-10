package com.polymorph.soaringcoach.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

/**
 * Uses the Point3d class as basis for calculations.  By convention:
 *  x = Latitude (North / South)
 *  y = Longitude (East / West)
 *  z = Altitude (in meters above mean sea level, or AMSL)
 * @author johanpretorius
 *
 */
public class GNSSPoint extends Point3d {
	private Date timestamp = new Date();
	
	/**
	 * Takes input like "B0948523340100S01925448EA00261002670080041315512952118-0065-7300100"
	 * and parses it into different fields.
	 * @param file_input
	 */
	public GNSSPoint(String file_input) {
		if (isValidGpsFix(file_input)) {
			// It's a GPS fix record, so we can convert it.
			
		} else {
			System.out.println("Discarded line: [" + file_input + "]");
		}
	}

	/**
	 * Checks if a line from the file is a valid GPS fix
	 * @param file_input
	 * @return
	 */
	private boolean isValidGpsFix(String file_input) {
		if (file_input == null) {
			return false;
		}
		
		if (!file_input.substring(0, 1).equals("B")) {
			return false;
		}

		if (!file_input.substring(24, 24).equals("A")) {
			return false;
		}
		
		return true; //All checks passed
	}
}
