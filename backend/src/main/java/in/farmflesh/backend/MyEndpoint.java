/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package in.farmflesh.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.farmflesh.in",
    ownerName = "backend.farmflesh.in",
    packagePath=""
  )
)
public class MyEndpoint {

    private static final Logger log = Logger.getLogger(MyEndpoint.class.getName());

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    /** A simple endpoint method that takes a email and says success back */
    @ApiMethod(name = "takeEmail")
    public MyBean takeEmail(@Named("name") String email) {
        MyBean response = new MyBean();
        log.warning(email);

        Date date = new Date();

        JSONObject jObject  = new JSONObject(email); // json
        Key customerKey = KeyFactory.createKey("Customer", jObject.getString("email"));


        Entity customer = new Entity("Customer", customerKey);
        try {
            customer.setProperty("name", jObject.getString("name"));
            customer.setProperty("email", jObject.getString("email"));
            customer.setProperty("date", date);
        }catch(JSONException ex){
            log.warning(ex.toString());
        }


/*        Entity customer = new Entity("Customer", customerKey);
        customer.setProperty("name", "Gok");
        customer.setProperty("email", email);
        customer.setProperty("date", date);*/

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(customer);



        response.setData("Success:, " + email);
        return response;
    }

}
