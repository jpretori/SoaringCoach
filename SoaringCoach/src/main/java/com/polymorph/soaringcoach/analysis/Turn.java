package com.polymorph.soaringcoach.analysis;

import java.util.Date;

public class Turn {
	public Date timestamp;
	public long duration;
	private boolean included_in_thermal = false;

	public Turn(Date timestamp, long duration) {
		this.timestamp = timestamp;
		this.duration = duration;
	}
	
	public String toString() {
		return "timestamp=["+timestamp+"], duration=["+duration+"]";
	}

	public boolean isIncludedInThermal() {
		return included_in_thermal;
	}

	public void setIncludedInThermal() {
		this.included_in_thermal = true;
	}
}