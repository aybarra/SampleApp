package com.example.mymodule.backend;

/**
 * Created by andrasta on 11/17/14.
 */

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}