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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

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
    private boolean isUpdate;

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hii web, " + name);
        getRoleByPassword(name);
        return response;
    }

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "takeId")
    public MyBean takeId(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hii takeId, " + name);
        return response;
    }

    /** A simple endpoint method that takes a email and says success back */
    @ApiMethod(name = "takeEmail")
    public MyBean takeEmail(@Named("name") String email) {
        MyBean response = new MyBean();
        log.warning(email);
        String repString = "Reg not success";
        JSONObject jObject  = new JSONObject(email);
        log.info("name:" + jObject.getString("name"));
        log.info("email:" + jObject.getString("email"));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (!isAlreadyRegUser(jObject.getString("email"))) {
            Entity customer = new Entity("Customerss", jObject.getString("email"));
            Date date = new Date();
            try {
                customer.setProperty("name", jObject.getString("name"));
                customer.setProperty("email", jObject.getString("email"));
                customer.setProperty("date", date);
                datastore.put(customer);
                repString = "Reg success";
            }catch(Exception ex){
                log.warning(ex.toString());
            }
        }else {
            repString = "Already reg";
        }
        response.setData("Success:, " + repString);
        return response;
    }

    /** A simple endpoint method that takes a email and says success back */
    @ApiMethod(name = "setRole")
    public MyBean setRole(@Named("name") String role) {
        MyBean response = new MyBean();
        log.warning(role);
        String repString = "Role not set";
        JSONObject jObject  = new JSONObject(role);
        log.info("role:" + jObject.getString("role"));
        log.info("password:" + jObject.getString("password"));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (!isAlreadyRoleSet(jObject.getString("role"))) {
            Entity customer = new Entity("Rolee", jObject.getString("role"));
            Date date = new Date();
            try {
                customer.setProperty("role", jObject.getString("role"));
                customer.setProperty("password:", jObject.getString("password"));
                customer.setProperty("date", date);
                datastore.put(customer);
                repString = "Role set";
            }catch(Exception ex){
                log.warning(ex.toString());
            }
        }else {
            repString = "Role already set";
            if(isUpdate) {
                log.info("Have to update");
                updatePassword(jObject.getString("role"), jObject.getString("password"));
                repString="Role updated";
            }
        }
        response.setData("Success:, " + repString);
        return response;
    }

    private void getRoleByPassword(String password) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        log.info("getRoleByPassword");
        Key grpKey = KeyFactory.createKey("Rolee", password);
        log.info("grpKey:" + grpKey.toString());
        Query.Filter filter = new Query.FilterPredicate(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.EQUAL, grpKey);
        log.info("filter:" + filter.toString());
        Query q0 = new Query("Rolee").setFilter(filter);
        log.info("q0:" + q0.toString());
        PreparedQuery pq0 = datastore.prepare(q0);
        log.info("pq0:" + pq0.toString());
        Entity result = pq0.asSingleEntity();
        log.info("result:" + result.toString());
        log.info(result.getProperty("role").toString());
    }

    private void updatePassword(String role, String password) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity customer = new Entity("Rolee", role);
        Date date = new Date();
        try {
            customer.setProperty("role", role);
            customer.setProperty("password:", password);
            customer.setProperty("date", date);
            datastore.put(customer);
        }catch(Exception ex){
            log.warning(ex.toString());
        }
    }

    private boolean isAlreadyRoleSet(String role) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key roleKey=KeyFactory.createKey("Rolee", role);
        try {
            Entity roleName = datastore.get(roleKey);
            log.info("Role " + roleName + " already set");
            isUpdate= true;
            return true;
        }catch(Exception ex){
            log.warning(ex.toString());
            return false;
        }
    }

    private boolean isAlreadyRegUser(String email) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key customerKey=KeyFactory.createKey("Customerss", email);
        try {
            Entity customer = datastore.get(customerKey);
            log.info("Customer " + customer + " already register");
            return true;
        }catch(Exception ex){
            log.warning(ex.toString());
            return false;
        }
    }
}
