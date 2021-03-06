package com.example.mymodule.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;
import java.util.logging.Logger;
import javax.inject.Named;

import static com.example.mymodule.backend.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 *
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "registration", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.mymodule.example.com", ownerName = "backend.mymodule.example.com", packagePath=""))
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());

    /**
     * Register a device to the backend
     *
     * @param regId The Google Cloud Messaging registration Id to add
     * @param gtId
     */
    @ApiMethod(name = "register")
    public void registerDevice(@Named("regId") String regId, @Named("gtId") String gtId) {
        if(findRecord(regId) != null) {
            log.info("Device " + regId + " already registered, skipping register");
            return;
        }
        RegistrationRecord record = new RegistrationRecord();
        record.setRegId(regId);
        record.setGtUsername(gtId);
        ofy().save().entity(record).now();
    }

    /**
     * Unregister a device from the backend
     *
     * @param gtId The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterDevice(@Named("gtId") String gtId) {
        RegistrationRecord record = findRecord(gtId);
        if(record == null) {
            log.info("Device " + gtId + " not registered, skipping unregister");
            return;
        }
        ofy().delete().entity(record).now();
    }

    /**
     * Return a collection of registered devices
     *
     * @param count The number of devices to list
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "listDevices")
    public CollectionResponse<RegistrationRecord> listDevices(@Named("count") int count) {
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(count).list();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }

    private RegistrationRecord findRecord(String gtId) {
        return ofy().load().type(RegistrationRecord.class).filter("gtId", gtId).first().now();
    }

}