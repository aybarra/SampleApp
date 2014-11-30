package com.example.mymodule.backend;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;

import static com.example.mymodule.backend.OfyService.ofy;

/**
 * An endpoint to send messages to devices registered with the backend
 *
 * For more information, seed
 * https://developers.google.com/appengine/docs/java/endpoints/
 *
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "group", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.mymodule.example.com", ownerName = "backend.mymodule.example.com", packagePath=""))
public class GroupEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /** Api Keys can be obtained from the google cloud console */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    @ApiMethod(name = "getGroupUsers")
    public MyBean getGroupUsers(@Named("groupId") String groupId) throws IOException {
        Group g = new Group(groupId);
        MyBean response = new MyBean();
        JSONObject listOfUsers = g.getGroupUsers();

        if(listOfUsers != null){
            response.setData(listOfUsers.toString());
        } else{
            response.setData("ERROR COMING FROM GROUP REQUEST");
        }

        return response;
    }

    @ApiMethod(name="getGroups")
    public MyBean listGroups(@Named("gtId") String gtId) throws IOException {
        User u = new User(gtId);
        MyBean response = new MyBean();
		JSONObject g = u.getGroups();
//		System.out.println(g.toString());
        if(g!=null){
            log.info(g.toString());
            response.setData(g.toString());
        }else {
            response.setData("ERROR COMING FROM GET GROUPS");
            log.severe("QUERY IN GET GROUPS RETURNED NULL");
        }

        return response;
    }
}
