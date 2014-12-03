package com.example.mymodule.backend;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//The Worker servlet should be mapped to the "/worker" URL.
public class Notification extends HttpServlet {
	private static final long serialVersionUID = 1L;

protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
	 //obtain data
	 String gtID = request.getParameter("gtID");
     String eventName = request.getParameter("eventName");
     String startTime = request.getParameter("startTime");

    // lat, longe
     String location = request.getParameter("location");

     // Do something


    String messagepayload = "{\"action\": \"reminder\", \"eventName\":";
    messagepayload+=eventName;
    messagepayload+=", \"eventStartTime\":";
    messagepayload+=startTime;

    messagepayload+=", \"eventLocation\": \"KlausBuilding\", \"eventCoordinates\":";
    messagepayload+=location;

    messagepayload+=", \"eventEndTime\": ";
    messagepayload+=startTime;
//    \"2014-12-116: 30: 00\"}";

    User user = new User(gtID);
    user.getRegistrationID();

    MessagingEndpoint me = new MessagingEndpoint();
    JSONObject payload = new JSONObject();
    payload.put("action", messagepayload);
    me.sendMessage(payload.toString());
}
}
