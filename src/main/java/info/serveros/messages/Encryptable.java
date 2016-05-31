package info.serveros.messages;

import javax.json.stream.JsonGenerator;
import java.util.Date;
import javax.json.JsonObject;
import info.serveros.Encrypter;

/**
 *  An encryptable class - keeps track of when it was created.
 */
public abstract class Encryptable extends info.serveros.JSONable {
    public static long MAX_SAFE_NONCE = 9007199254740991L;

    /**
     *  The time at which this Encryptable was created.
     */
    private final Date timestamp;

    /**
     *  Constructor.  Specify a Date.
     *
     *  @param ts The timestamp wanted.
     */
    public Encryptable(Date ts) {
        this.timestamp = (Date) ts.clone();
    }//Encryptable(Date)*/

    /**
     *  Constructor. Use the current Date.
     */
    public Encryptable() {
        this(new Date());
    }//Encryptable()*/

    /**
     *  Constructor.  Specify a timestamp.
     *
     *  @param timestamp the timestamp in milliseconds since the epoch.
     */
    public Encryptable(long timestamp) {
        this(new Date(timestamp));
    }//Encryptable(long)*/

    /**
     *  Constructor.  Unpack from a Json Object.
     *
     *  @param o the JSON object.
     */
    public Encryptable(JsonObject o) {
        this(o.getJsonNumber("ts").longValue());
    }//Encryptable(JsonObject)*/

    /**
     *  Constructor.  Unpack from a JSON encoded string.
     *
     *  @param s The JSON encoded string.
     */
    public Encryptable(String s) {
        this(Encryptable.unJSON(s));
    }//Encryptable(String)*/

    /**
     *  Get the timestamp.
     */
    public Date getTimestamp() {
        return (Date) this.timestamp.clone();
    }//getTimestamp()*/

    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected void jsonHelper(JsonGenerator g) {
        g.write("ts", this.timestamp.getTime());
    }

    /**
     *  Decide if this Encryptable is Stale.
     *
     *  @return true if the timestamp is more that STALE_REQUEST_TOLERANCE away from the current timestamp.
     */
    public boolean isStale() {
        return Math.abs(System.currentTimeMillis() - this.timestamp.getTime()) > Encrypter.STALE_REQUEST_TOLERANCE;
    }//isStale(long)*/

    /**
     *  Generate a Nonce.
     *
     *  @return a random positive integer.
     */
    public static long generateNonce() {
        return (long) Math.floor(Math.random() * Encryptable.MAX_SAFE_NONCE);
    }//generateNonce()*/

}//Encryptable*/
