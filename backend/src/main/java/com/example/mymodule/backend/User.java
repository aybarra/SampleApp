package com.example.mymodule.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class User {
	//private variables
	private String gtid;
	@SuppressWarnings("unused")
	private long privateKey;	//No idea what the privateKey is for.
	private String registrationId;
	private String locLatitude;
	private String locLongitude;
	
	//Constructors
	public User(String GTID){
		//sets default values
		gtid=GTID;
		String query = "https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/user/v1/user/"+gtid;
		String USER_AGENT = "Chrome/38.0";
		try {
			URL obj = new URL(query);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine, jsonstring="";
				
			while ((inputLine = in.readLine()) != null) {
				jsonstring+=inputLine+"\n";
			}
			in.close();
			JSONObject json = (JSONObject) new JSONParser().parse(jsonstring);
			registrationId=(String) json.get("registrationId");
			locLatitude= json.get("locLatitude").toString();
			locLongitude=json.get("locLongitude").toString();
		}
		catch (Exception e1){
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
	}
	
	//Methods
	public void setRegistrationID(String GTID, String RegistrationID){
		gtid = GTID;
		registrationId=RegistrationID;
		String query = "https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/user/v1/user";
		String s = "{\"registrationId\":\""+registrationId+"\",\"gtid\":\""+gtid+"\"}";
		String USER_AGENT = "Chrome/38.0";
		try {
			URL obj = new URL(query);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("PUT");
			con.addRequestProperty("Content-Type", "application/json");
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(s);
			out.close();
			con.getInputStream();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public String getLocation(){
		return (locLongitude.toString()+","+locLatitude.toString());
	}
	
	public void updateLocation(String GTID, String longitude, String latitude){
		gtid = GTID;
		locLatitude=latitude;
		locLongitude=longitude;
		String s = String.format("latitude=%.5f&longitude=%.5f", Float.parseFloat(locLatitude), Float.parseFloat(locLongitude));
		String query = "https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/user/v1/location/"+gtid+"?"+s;
		String USER_AGENT = "Chrome/38.0";
		try {
			URL obj = new URL(query);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("PUT");
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(s);
			out.close();
			con.getInputStream();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public JSONObject getGroups(){
		String query = "https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/user/v1/listGroups/";
		String USER_AGENT = "Chrome/38.0";
		try {
			URL obj = new URL(query+gtid);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine, jsonstring="";
				
			while ((inputLine = in.readLine()) != null) {
				jsonstring+=inputLine+"\n";
			}
			in.close();
			JSONObject json = (JSONObject) new JSONParser().parse(jsonstring);
			json.remove("etag");
			json.remove("kind");
			return json;
		}
		catch (Exception e1){
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args){
//		User u = new User("gburdell");
//		JSONObject g = u.getGroups();
//		System.out.println(g.toString());
//	}
}
