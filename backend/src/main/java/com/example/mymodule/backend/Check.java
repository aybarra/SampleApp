package com.example.mymodule.backend;

//The Enqueue servlet should be mapped to the "/enqueue" URL.
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class Check extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
			//Hardcoded Events Table for testing
			ArrayList<Event> eventDB = new ArrayList<Event>();
			DateTimeZone zone = DateTimeZone.forID("US/Eastern");
			//userName, eventName, longitude, latitude, startTime
			DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").withZone(zone);
			eventDB.add(new Event("user1", "class1", "88.34, 88.56", df.parseDateTime("2014/11/29 23:51:00")));		
			eventDB.add(new Event("user2", "class3",  "88.34, 88.56", df.parseDateTime("2014/11/29 23:52:00")));
			eventDB.add(new Event("user3", "class2", "88.34, 88.56", df.parseDateTime("2014/11/29 23:51:00")));		
			eventDB.add(new Event("user1", "class3",  "88.34, 88.56", df.parseDateTime("2014/11/29 23:52:00")));
			
			//Add events for this day
			for(Event e: eventDB) {		
				DateTime now = new DateTime(zone);
				if(now.toLocalDate().equals(e.getStartTime().toLocalDate())) {
					//Start Time - Notification Time， default = 10 min before event
					long fixedTimeBefore = 1000 * 60 * 10;
					
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