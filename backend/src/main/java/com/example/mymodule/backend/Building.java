package com.example.mymodule.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Building {
private String id;
private String location;
public Building(String buildingid){
	id=buildingid;
	String query="https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/building/v1/building/"+id;
	String USER_AGENT = "Chrome/38.0";
	try{
		URL obj = new URL(query);
		HttpURLConnection con;
		con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		String jsonstring = new String();
			
		while ((inputLine = in.readLine()) != null) {
			jsonstring+=inputLine+"\n";
		}
		
		JSONObject building = (JSONObject) new JSONParser().parse(jsonstring);
		location=building.get("latitude")+","+building.get("longitude");
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	};
}

public String getlocation(){
	return location;
}

}
