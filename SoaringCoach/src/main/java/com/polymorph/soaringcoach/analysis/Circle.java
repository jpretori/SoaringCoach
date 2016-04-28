package com.polymorph.soaringcoach.analysis;

import java.util.Date;

import com.polymorph.soaringcoach.CHECK_TWICE_RULE;
import com.polymorph.soaringcoach.COMPASS_POINTS;

public class Circle {
	public Date timestamp;
	public long duration;
	private boolean included_in_thermal = false;

	public Circle(Date timestamp, long duration) {
		this.timestamp = timestamp;
		this.duration = duration;
	}
	
	public String toString() {
		String duration_s = duration < 10 ? "0" + String.valueOf(duration) : String.valueOf(duration);
		
		return "Duration (s) = ["+duration_s+"]";
	}

	public boolean isIncludedInThermal() {
		return included_in_thermal;
	}

	public void setIncludedInThermal() {
		this.included_in_thermal = true;
	}

	public boolean centeringCorrection() {
		// TODO Auto-generated method stub
		return false;
	}

	public CHECK_TWICE_RULE checkTwiceRuleFollowed() {
		// TODO Auto-generated method stub
		return CHECK_TWICE_RULE.NOT_APPLICABLE;
	}

	public int getAltitudeChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getClimbRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDriftDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public COMPASS_POINTS getDriftDirection() {
		// TODO Auto-generated method stub
		return COMPASS_POINTS.N;
	}

	void setCorrectiveMoveDetected(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public CHECK_TWICE_RULE getCheckTwiceRuleIndicator() {
		// TODO Auto-generated method stub
		return CHECK_TWICE_RULE.NOT_APPLICABLE;
	}
}