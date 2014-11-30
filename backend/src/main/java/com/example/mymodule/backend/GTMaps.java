package com.example.mymodule.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GTMaps {
    //Parameters
    private String origin = "origin=";
    private String destination = "&destination=";
    private String mode = "&mode=";
    private String key = "&key=";

    //Values
    private static String USER_AGENT = "Chrome/38.0";
    private String url = "https://maps.googleapis.com/maps/api/directions/json?";
    private String origlatlng = "33.778089,-84.396172";
    private String destlatlng = "33.775237,-84.396585";
    private String travelmode = "walking";
    private String API_Key = "AIzaSyAs4xB9DW2jdz5wgOHJ0kmPsMo8IsSnvis";
    //private String stop1latlng = ""; //To be used in later iterations
    //private String stop2latlng = ""; //To be used in later iterations

    //Main Query
    private static String query;

    //Constructors
    GTMaps(){
        setQuery();
    }

    GTMaps(String startloc, String endloc, String travmode){
        origlatlng=startloc;
        destlatlng=endloc;
        travelmode=travmode;
        setQuery();
    }

    public void setUserLocation(String loc){
        origlatlng=loc;
        setQuery();
    }

    public void setQuery(){
        query=url+origin+origlatlng+destination+destlatlng+mode+travelmode+key+API_Key;
    }

    public JSONObject runQuery(){
        URL obj;
        try {
            obj = new URL(query);
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

            JSONObject json = (JSONObject) new JSONParser().parse(jsonstring);
            in.close();
            return json;
        }
        catch (Exception e1){
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) throws IOException{
//        GTMaps gt1 = new GTMaps();
//        gt1.setUserLocation("33.778089,-84.396172");//Pass latitude,longitude here
//        JSONObject json = gt1.runQuery();
//        if(json.containsValue("INVALID_REQUEST")){
//            System.out.println("Well, THAT didn't work. Try again.");
//        } else{
//            System.out.println(json.toString());
//        }
//    }
}