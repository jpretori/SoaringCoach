package com.polymorph.soaringcoach.analysis;

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
	
	public GNSSPoint(String file_input) {
		
	}
}
