package com.example.mymodule.backend;

import org.joda.time.DateTime;

public class Event {
	private long eventID;
	private String gtID;
	private String eventName;
	private String location;
	private DateTime startTime;
	
	Event(String gtID, String eventName, String location, DateTime startTime) {
		this.setGtID(gtID);
		this.setEventName(eventName);
		this.setLocation(location);
		this.setStartTime(startTime);
	}

	public String getGtID() {
		return gtID;
	}

	public void setGtID(String gtID) {
		this.gtID = gtID;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
}
