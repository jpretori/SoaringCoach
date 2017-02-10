package com.polymorph.soaringcoach.analysis.parsing;

public class PICName {
	public String recordType;
	public String picName;
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String text) {
		if (text != null) {
			try {
				this.picName = text.substring(text.indexOf(":") + 1);
			} catch (IndexOutOfBoundsException e) {
				this.picName = "";
			}
		} else {
			this.picName = text;
		}
	}
	
}
