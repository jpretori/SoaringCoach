package com.polymorph.soaringcoach.persistence;

public class IgcBRecord {

	private int id;
	private String filename;
	private String timestamp;
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
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
	public String getAltitude_ok() {
		return altitude_ok;
	}
	public void setAltitude_ok(String altitude_ok) {
		this.altitude_ok = altitude_ok;
	}
	public int getPressure_altitude() {
		return pressure_altitude;
	}
	public void setPressure_altitude(int pressure_altitude) {
		this.pressure_altitude = pressure_altitude;
	}
	public int getGnss_altitude() {
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
	
}
