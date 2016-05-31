package info.serveros;

import info.serveros.algorithms.*;
import java.util.Date;
import javax.json.stream.JsonGenerator;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 *  Some Credentials!
 */
public class Credentials extends JSONable {

    /**
     *  The application that the Credentials are for - the requester, if you are the requested, or the
     *  requested, if you are the requester.
     */
    public final JsonValue application;

    /**
     *  The Credentials ID. (Public)
     */
    public final String id;

    /**
     *  The Credentials Key. (Secret)
     */
    public final String key;

    /**
     *  The Hash Algorithm to be used to do any hashing.
     */
    public final HashSpec algorithm;

    /**
     *  The expiration date of the Credentials.
     */
    private final Date expires;

    /**
     *  Null except for at the Provider, where it carries some arbitrary AuthData from the Master.
     */
    public final JsonValue authData;

    /**
     *  Constructor.
     *
     *  @param application The application at the other end of any communication with these Credentials.
     *  @param id The Public ID.
     *  @param key the Secret Key.
     *  @param algorithm THe Hash Algorithm to use.
     *  @param expires The Date at which the credentials Expire.
     */
    public Credentials(JsonValue application, String id, String key, HashSpec algorithm, Date expires) {
        this(application, id, key, algorithm, expires, null);
    }//Credentials(JsonValue, String, String, HashSpec, Date)*/

    /**
     *  Constructor.
     *
     *  @param application The application at the other end of any communication with these Credentials.
     *  @param id The Public ID.
     *  @param key the Secret Key.
     *  @param algorithm THe Hash Algorithm to use.
     *  @param expires The Date at which the credentials Expire.
     *  @param authData The data from the Authentication Master.
     */
    public Credentials(JsonValue application, String id, String key, HashSpec algorithm
                , Date expires, JsonValue authData
            ) {
        this.application = application;
        this.id = id;
        this.key = key;
        this.algorithm = algorithm;
        this.expires = (Date) expires.clone();
        this.authData = authData;
    }//Credentials(JsonValue, String, String, HashSpec, Date, JsonValue)*/

    /**
     *  Constructor.  Unpacks an Object.
     *
     *  @param o The Json Object.
     */
    public Credentials(JsonObject o) {
        this(
            o.get("application")
            , o.getString("id")
            , o.getString("key")
            , HashSpec.fromString(o.getString("algorithm"))
            , new Date(o.getJsonNumber("expires").longValue())
            , o.containsKey("authData") ? o.get("authData") : null
        );
    }//Credentials(JsonObject)*/

    /**
     *  Constructor.  Unpacks a JSON encoded String.
     *
     *  @param s The string representing some JSON encoded String.
     */
    public Credentials(String s) {
        this(JSONable.unJSON(s));
    }//Credentials(String)*/

    /**
     *  Help me JSON myself.
     *
     *  @param g The Json Generator to write to.
     */
    @Override
    protected void jsonHelper(JsonGenerator g) {
        g.write("application", this.application);
        g.write("id", this.id);
        g.write("key", this.key);
        g.write("algorithm", this.algorithm.toString());
        g.write("expires", this.expires.getTime());
        if (this.authData != null)
            g.write("authData", this.authData);
    }//jsonHelper(JsonGenerator)*/

    /**
     *  Get the Expiry.
     *
     *  @return the date at which these credentials expire.
     */
    public Date getExpiry() {
        return (Date) this.expires.clone();
    }//getExpiry()*/
}//Credentials*/
