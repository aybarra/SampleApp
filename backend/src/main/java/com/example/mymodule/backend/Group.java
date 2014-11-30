package com.example.mymodule.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class Group {
JSONObject group;

public Group(String GroupID){
	URL obj;
	String query="https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/group/v1/listUsers?groupid=";
	String USER_AGENT = "Chrome/38.0";
	try {
		obj = new URL(query+GroupID);
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
		
		group = (JSONObject) new JSONParser().parse(jsonstring);
		group.remove("etag");
		group.remove("kind");
		in.close();
}
catch (Exception e1){
// TODO Auto-generated catch block
e1.printStackTrace();
}
}

public void addUser(String GTID, String GroupID){
	
}

public JSONObject getGroupUsers(){
	return group;
}

//public static void main(String[] args){
//	Group g = new Group("1");
//	JSONObject a= g.getGroupUsers();
//	System.out.println(a.toString());
//}
}
