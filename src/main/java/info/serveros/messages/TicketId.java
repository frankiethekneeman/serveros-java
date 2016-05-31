package info.serveros.messages;

import info.serveros.algorithms.*;
import info.serveros.Encrypter.CryptoMessage;
import info.serveros.OneTimeCredentials;
import java.util.Date;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

/**
 *  A Proof of Credentials.
 */
public class TicketId extends Encryptable {

    /**
     *  The requester.
     */
    public final JsonValue requester;

    /**
     *  A nonce generated by the server.
     */
    public final long serverNonce;

    /**
     *  The first nonce generated by the requester.
     */
    public final long requesterNonce;

    /**
     *  The second nonce generated by the requester.
     */
    public final long finalNonce;

    /**
     *  The IV to be used for the TicketAck.
     */
    public final String iv;

    /**
     *  Constructor.
     *
     *  @param requester The appliction that requested credentials.
     *  @param serverNonce The nonce generated by the server.
     *  @param requesterNonce The first nonce generated by the requester.
     *  @param finalNonce The second nonce generated by the requester.
     *  @param iv the Initial Vector to be used by the TicketAck.
     */
    public TicketId(JsonValue requester, long serverNonce, long requesterNonce, long finalNonce , String iv) {
        super();
        this.requester = requester;
        this.serverNonce = serverNonce;
        this.requesterNonce = requesterNonce;
        this.finalNonce = finalNonce;
        this.iv = iv;
    }//TicketId(String, long, long, long, String)*/


    /**
     *  Constructor - generates a new nonce.
     *
     *  @param requester The appliction that requested credentials.
     *  @param serverNonce The nonce generated by the server.
     *  @param requesterNonce The first nonce generated by the requester.
     *  @param iv the Initial Vector to be used by the TicketAck.
     */
    public TicketId(JsonValue requester, long serverNonce, long requesterNonce, String iv) {
        this(requester, serverNonce, requesterNonce, Encryptable.generateNonce(), iv);
    }//TicketId(String, long, long, String)*/

    /**
     *  Constructor - unpacks a JSON object.
     *
     *  @param o The Json Object.
     */
    public TicketId(JsonObject o) {
        super(o);
        this.requester = o.get("id");
        this.serverNonce = o.getJsonNumber("serverNonce").longValue();
        this.requesterNonce = o.getJsonNumber("requesterNonce").longValue();
        this.finalNonce = o.getJsonNumber("finalNonce").longValue();
        this.iv = o.getString("iv");
    }

    /**
     *  Constructor.  Unpacks a JSON String.
     *
     *  @param s A string that represents a JSON Encoded TicketId.
     */
    public TicketId(String s) {
        this(Encryptable.unJSON(s));
    }//TIcketId(String)*/


    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected void jsonHelper(JsonGenerator g) {
        super.jsonHelper(g);
        g.write("id", this.requester);
        g.write("serverNonce", this.serverNonce);
        g.write("requesterNonce", this.requesterNonce);
        g.write("finalNonce", this.finalNonce);
        g.write("iv", this.iv);
    }//jsonHelper(g)*/
}//TicketId*/
