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

package soaringcoach.analysis.parsing;

import java.util.Date;

public class GNSSPointData {
	public Date timestamp;
	public String filename;
	public String record_type;
	public String latitude_degrees;
	public String latitude_minutes;
	public String latitudeEquatorRef;
	public String longitude_degrees;
	public String longitude_minutes;
	public String longitude_greenwich_ref;
	public String altitudeOk;
	public int pressure_altitude;
	public int gnss_altitude;
	public String other;

	public GNSSPointData() {
		this.timestamp = null;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getRecordType() {
		return record_type;
	}
	public void setRecordType(String record_type) {
		this.record_type = record_type;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getLatitudeDegrees() {
		return latitude_degrees;
	}
	public void setLatitudeDegrees(String latitude_degrees) {
		this.latitude_degrees = latitude_degrees;
	}

	public String getLatitudeMinutes() {
		return latitude_minutes;
	}
	public void setLatitudeMinutes(String latitude_minutes) {
		this.latitude_minutes = latitude_minutes;
	}

	public String getLatitudeEquatorRef() {
		return latitudeEquatorRef;
	}
	public void setLatitudeEquatorRef(String latitude_equator_ref) {
		this.latitudeEquatorRef = latitude_equator_ref;
	}

	public String getLongitudeDegrees() {
		return longitude_degrees;
	}
	public void setLongitudeDegrees(String longitude_degrees) {
		this.longitude_degrees = longitude_degrees;
	}

	public String getLongitudeMinutes() {
		return longitude_minutes;
	}
	public void setLongitudeMinutes(String longitude_minutes) {
		this.longitude_minutes = longitude_minutes;
	}

	public String getLongitudeGreenwichRef() {
		return longitude_greenwich_ref;
	}
	public void setLongitudeGreenwichRef(String longitude_greenwich_ref) {
		this.longitude_greenwich_ref = longitude_greenwich_ref;
	}

	public String getAltitudeOK() {
		return altitudeOk;
	}
	public void setAltitudeOK(String altitude_ok) {
		this.altitudeOk = altitude_ok;
	}
	
	public int getPressureAltitude() {
		return pressure_altitude;
	}
	public void setPressureAltitude(int pressure_altitude) {
		this.pressure_altitude = pressure_altitude;
	}
	
	public int getGnssAltitude() {
		return gnss_altitude;
	}
	public void setGnssAltitude(int gnss_altitude) {
		this.gnss_altitude = gnss_altitude;
	}
	
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}