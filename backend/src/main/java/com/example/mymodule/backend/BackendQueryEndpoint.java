package com.example.mymodule.backend;

        import com.google.android.gcm.server.Constants;
        import com.google.android.gcm.server.Message;
        import com.google.android.gcm.server.Result;
        import com.google.android.gcm.server.Sender;
        import com.google.api.server.spi.config.Api;
        import com.google.api.server.spi.config.ApiNamespace;

        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
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
@Api(name = "queryBackend", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.mymodule.example.com", ownerName = "backend.mymodule.example.com", packagePath=""))
public class BackendQueryEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /** Api Keys can be obtained from the google cloud console */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    public void sendMessage(@Named("action") String message) throws IOException {
        if(message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("action", message).build();
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(10).list();
        for(RegistrationRecord record : records) {
            Result result = sender.send(msg, record.getRegId(), 5);
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.getRegId());
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
                    record.setRegId(canonicalRegId);
                    ofy().save().entity(record).now();
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    ofy().delete().entity(record).now();
                }
                else {
                    log.warning("Error when sending message : " + error);
                }
            }
        }
    }

    public JSONObject fetchUserInfo() throws IOException {

        URL obj;
        try {
            obj = new URL("https://1-dot-rlr-gtnow-backend.appspot.com/_ah/api/user/v1/user/gburdell");
            HttpURLConnection con;
            con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String jsonstring = new String();

            while ((inputLine = in.readLine()) != null) {
                jsonstring += inputLine + "\n";
            }

            JSONObject json = (JSONObject) new JSONParser().parse(jsonstring);
            in.close();
            return json;
        } catch (Exception e) {
            return null;
        }
    }
}
