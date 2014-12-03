package com.example.mymodule.backend;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import com.example.mymodule.backend.Building;

public class Check extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
			//Hardcoded Events Table for testing
			ArrayList<Event> eventDB = new ArrayList<Event>();
			DateTimeZone zone = DateTimeZone.forID("US/Eastern");
			//userName, eventName, longitude, latitude, startTime
			DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'").withZone(zone);

        URL obj;
        String query = "https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/event/v1/event";
        String USER_AGENT="Chrome/38.0";
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
            json.remove("etag");
            json.remove("kind");
            for(int i=0;i<((JSONArray) json.get("items")).size();i++){
                JSONObject j = (JSONObject) ((JSONArray) json.get("items")).get(i);
                Building b = new Building(j.get("buildingID").toString());
                eventDB.add(new Event((String) j.get("gtid"), (String) j.get("name"), b.getlocation(), df.parseDateTime((String) j.get("startTime"))));
            }
        }
        catch (Exception e1){
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
			//eventDB.add(new Event("user1", "class1", "88.34, 88.56", df.parseDateTime("2014/11/29 23:51:00")));
			//eventDB.add(new Event("user2", "class3",  "88.34, 88.56", df.parseDateTime("2014/11/29 23:52:00")));
			//eventDB.add(new Event("user3", "class2", "88.34, 88.56", df.parseDateTime("2014/11/29 23:51:00")));
			//eventDB.add(new Event("user1", "class3",  "88.34, 88.56", df.parseDateTime("2014/11/29 23:52:00")));
			
			//Add events for this day
			for(Event e: eventDB) {		
				DateTime now = new DateTime(zone);
				if(now.toLocalDate().equals(e.getStartTime().toLocalDate())) {
					//Start Time - Notification Time， default = 10 min before event
					long fixedTimeBefore = 1000 * 60 * 30;
					
					Period timeleft = new Period(now, e.getStartTime());
					long timeToNotify = timeleft.toStandardDuration().getMillis() - fixedTimeBefore;
					if(timeToNotify >= 0) {
						enQueue(e.getGtID(), e.getEventName(), e.getStartTime().toString(df), e.getLocation(), timeToNotify);
					}
				}
			}
	}
	
	private void enQueue(String gtID, String eventName, String startTime, String location, long timeToNotify) {
		// Add the task to the default queue.
		   Queue queue = QueueFactory.getDefaultQueue();
		   queue.add(withUrl("/notification")
				 .param("gtID", gtID)
		  		 .param("eventName", eventName)
		  		 .param("startTime", startTime)
		  		 .param("location", location)
		  		 .countdownMillis(timeToNotify) 
		   );
	}
	

	
}