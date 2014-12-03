package com.example.mymodule.backend;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.example.mymodule.backend.Building;


public class Events {
	private long eventID;
	private String title;
	private String location; //Preferably latitude and longitude in the following format: "33.778089,-84.396172"
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean recurring;
	private static String daysOfTheWeek = "" ;
	
	public Events(long ID, String name, String loc, LocalTime startT, LocalTime endT, LocalDate startD, LocalDate endD, boolean recur,String days){
		eventID=ID;
		title = name;
		location = loc;
		startTime = startT;
		endTime = endT;
		startDate = startD; 
		endDate = endD;
		recurring = recur;
		daysOfTheWeek = days;
	}
	
	public Events(JSONObject j){
		//JSONArray itemarray = (JSONArray) j.get("items");
		//JSONObject events = (JSONObject) itemarray.get(0);
		eventID = Long.parseLong((String) j.get("id"));
		title = (String) j.get("name");
		long locid=Long.parseLong((String) j.get("buildingID"));
		Building b = new Building(""+locid);
		location = b.getlocation();
		startTime = new LocalTime(hour(j.get("startTime")),minute(j.get("startTime")));
		endTime = new LocalTime(hour(j.get("endTime")),minute(j.get("endTime")));
		startDate = new LocalDate(year(j.get("startTime")),month(j.get("startTime")),day(j.get("startTime")));
		endDate = new LocalDate(year(j.get("endTime")),month(j.get("endTime")),day(j.get("endTime")));
		recurring = ((endDate.getWeekOfWeekyear() - startDate.getWeekOfWeekyear())>1);
		//daysOfTheWeek = days;		//not present in backend api
	}
	
	private int hour(Object o){
		String s = (String) o;
		return Integer.parseInt((s.substring(s.indexOf("T")+1, s.indexOf(":"))));
	}
	
	private int minute(Object o){
		String s = (String) o;
		return Integer.parseInt(s.substring(s.indexOf(":")+1).substring(0, s.substring(s.indexOf(":")+1).indexOf(":")));
	}
	
	private int month(Object o){
		String s = (String) o;
		return Integer.parseInt(s.substring(s.indexOf("-")+1).substring(0,s.substring(s.indexOf("-")+1).indexOf("-")));
	}
	
	private int day(Object o){
		String s = (String) o;
		return Integer.parseInt(s.substring(s.indexOf("-")+1,s.indexOf("T")).substring(s.substring(s.indexOf("-")+1,s.indexOf("T")).indexOf("-")+1));
	}
	
	private int year(Object o){
		String s = (String) o;
		return Integer.parseInt(s.substring(0,s.indexOf("-")));
	}
	
	public String getLoc(){
		return location;
	}
	
	public LocalDate getStartDate(){
		return startDate;
	}
	
	public LocalTime getStartTime(){
		return startTime;
	}
	
	public boolean eventtoday(){
		if(daysOfTheWeek.equals("")){
		return true; //This will change once backend api offers this information
		}else{
		DateFormat dateFormat = new SimpleDateFormat("F");
		//get current date time with Date()
		Date date = new Date();
		String index = dateFormat.format(date);
		int i = Integer.parseInt(index);
		if (daysOfTheWeek.charAt(i)=='1'){
			return true;
		}
		else{
			return false;
		}
		}
	}
	
//	public static void main(String[] args){
//
//	}
}
