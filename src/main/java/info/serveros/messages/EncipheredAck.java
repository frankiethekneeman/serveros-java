
package info.serveros.messages;

import info.serveros.algorithms.*;
import info.serveros.Encrypter.CryptoMessage;
import info.serveros.OneTimeCredentials;
import java.util.Date;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 *  An acknowledgement of received credentials.
 */
public class EncipheredAck extends Encryptable {

    /**
     *  The nonce generated by the server.
     */
    public final String message;

    /**
     *  Constructor.
     *
     *  @param serverNonce The nonce generated by the server.
     *  @param requesterNonce the initial nonce generated by the Requester.
     *  @param finalNonce the final nonce generated by the requester.
     */
    public EncipheredAck(String message) {
        super();
        this.message = message;
    }//TicketAck(long, long, long)*/

    /**
     *  Constructor.  Unpacks a Json Object.
     *
     *  @param o the Json Object.
     */
    public EncipheredAck(JsonObject o) {
        this(o.getString("message"));
    }//TicketAck(JsonObject)*/


    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected void jsonHelper(JsonGenerator g) {
        g.write("message", this.message);
    }//jsonHelper(g)*/
}//TicketAck*/