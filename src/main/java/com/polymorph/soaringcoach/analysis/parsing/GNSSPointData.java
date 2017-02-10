package com.polymorph.soaringcoach.analysis.parsing;

import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
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
	
	@Field(offset=1, length=1)
	public String getRecordType() {
		return record_type;
	}
	public void setRecordType(String record_type) {
		this.record_type = record_type;
	}
	
	@Field(offset=2, length=6)
	@FixedFormatPattern("HHmmss")
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Field(offset=8, length=2, align=Align.RIGHT)
	public String getLatitudeDegrees() {
		return latitude_degrees;
	}
	public void setLatitudeDegrees(String latitude_degrees) {
		this.latitude_degrees = latitude_degrees;
	}

	@Field(offset=10, length=5, align=Align.RIGHT)
	public String getLatitudeMinutes() {
		return latitude_minutes;
	}
	public void setLatitudeMinutes(String latitude_minutes) {
		this.latitude_minutes = latitude_minutes;
	}

	@Field(offset=15, length=1)
	public String getLatitudeEquatorRef() {
		return latitudeEquatorRef;
	}
	public void setLatitudeEquatorRef(String latitude_equator_ref) {
		this.latitudeEquatorRef = latitude_equator_ref;
	}

	@Field(offset=16, length=3, align=Align.RIGHT)
	public String getLongitudeDegrees() {
		return longitude_degrees;
	}
	public void setLongitudeDegrees(String longitude_degrees) {
		this.longitude_degrees = longitude_degrees;
	}

	@Field(offset=19, length=5, align=Align.RIGHT)
	public String getLongitudeMinutes() {
		return longitude_minutes;
	}
	public void setLongitudeMinutes(String longitude_minutes) {
		this.longitude_minutes = longitude_minutes;
	}

	@Field(offset=24, length=1)
	public String getLongitudeGreenwichRef() {
		return longitude_greenwich_ref;
	}
	public void setLongitudeGreenwichRef(String longitude_greenwich_ref) {
		this.longitude_greenwich_ref = longitude_greenwich_ref;
	}

	@Field(offset=25, length=1)
	public String getAltitudeOK() {
		return altitudeOk;
	}
	public void setAltitudeOK(String altitude_ok) {
		this.altitudeOk = altitude_ok;
	}
	
	@Field(offset=26, length=5, align=Align.RIGHT, paddingChar='0')
	public int getPressureAltitude() {
		return pressure_altitude;
	}
	public void setPressureAltitude(int pressure_altitude) {
		this.pressure_altitude = pressure_altitude;
	}
	
	@Field(offset=31, length=5, align=Align.RIGHT, paddingChar='0')
	public int getGnssAltitude() {
		return gnss_altitude;
	}
	public void setGnssAltitude(int gnss_altitude) {
		this.gnss_altitude = gnss_altitude;
	}
	
	@Field(offset=36, length=100)
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}