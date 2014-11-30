package com.example.mymodule.backend;

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
     String location = request.getParameter("location");
     // Do something
 }
}
