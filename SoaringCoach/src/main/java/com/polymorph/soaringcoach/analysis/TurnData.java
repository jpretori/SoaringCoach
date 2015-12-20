package com.polymorph.soaringcoach.analysis;

import java.util.Date;

public class TurnData {
	public Date timestamp;
	public long duration;

	public TurnData(Date timestamp, long duration) {
		this.timestamp = timestamp;
		this.duration = duration;
	}
}