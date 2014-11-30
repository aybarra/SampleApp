package com.example.mymodule.backend;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.search.GeoPoint;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.inject.Named;
import java.util.logging.Logger;

/** An endpoint class we are exposing */
@Api(name = "integration",
        version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.mymodule.example.com", ownerName = "backend.mymodule.example.com", packagePath=""))
public class TestEP {
    private static final Logger log = Logger.getLogger(TestEP.class.getName());

    /** A simple endpoint method that takes a lat long and returns coordinates received and sends nav directions over gcm */
    @ApiMethod(name = "currentLocation")
    public MyBean currentLocation(@Named("latitude") Double latitude, @Named("longitude") Double longitude) {
        GeoPoint gp = new GeoPoint(latitude, longitude);
        MyBean response = new MyBean();

        if(gp==null){
            response.setData("Invalid parameters for location");
//            response.setData("Hi, user located at:" + latitude+", "+longitude);
            return response;
        }

        GTMaps gt1 = new GTMaps();
        gt1.setUserLocation(""+latitude+","+longitude);//Pass latitude,longitude here
        JSONObject mapsResponse = gt1.runQuery();


        if(null == mapsResponse ||(null != mapsResponse && mapsResponse.containsValue("INVALID_REQUEST"))){
            response.setData("Your GPS coordinates were valid but the walking directions api returned an error");
        } else if(null != mapsResponse) {
            response.setData(mapsResponse.toString());

            // Make call to message the directions over gcm
            MessagingEndpoint me = new MessagingEndpoint();
            try {
                me.sendMessage(mapsResponse.toString());
            } catch (IOException e) {
//                e.printStackTrace();
                log.info("error occurred sending the ecm payload");
            }
        }


        return response;
    }

}

