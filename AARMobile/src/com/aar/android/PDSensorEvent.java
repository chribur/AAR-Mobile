package com.aar.android;

public class PDSensorEvent {

	private String mName = "";
	private String mValue = "";
	
	public PDSensorEvent(String name, String value) {
		mName = name;
		mValue = value;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getValue() {
		return mValue;
	}
	
	
}
