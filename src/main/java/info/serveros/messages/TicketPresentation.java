package info.serveros.messages;

import info.serveros.algorithms.*;
import info.serveros.Encrypter.CryptoMessage;
import info.serveros.OneTimeCredentials;
import java.util.Date;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 *  A Presentation of credentials
 */
public class TicketPresentation extends Encryptable {

    /**
     *  An enciphered TicketId
     */
    public final String id;

    /**
     *  The ticket being presented.
     */
    public final CryptoMessage ticket;

    /**
     *  Constructor.
     *
     *  @param id The enciphered TicketId
     *  @param ticket The ticket.
     */
    public TicketPresentation(String id, CryptoMessage ticket) {
        super();
        this.id = id;
        this.ticket = ticket;
    }//TicketPresentation(id, CryptoMessage)*/

    /**
     *  Constructor.  Unpacks a Json Object.
     *
     *  @param o The Json Object.
     */
    public TicketPresentation(JsonObject o) {
        super();//Timestamp doesn't matter.
        this.id = o.getString("id");
        this.ticket = new CryptoMessage(o.getJsonObject("ticket"));
    }//TicketPresentation(JsonObject)*/

    /**
     *  Constructor.  Unpacks a JSON String.
     *
     *  @param s A string that represents a JSON Encoded Credential Request.
     */
    public TicketPresentation(String s) {
        this(Encryptable.unJSON(s));
    }//TicketPresentation(String)*/


    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected void jsonHelper(JsonGenerator g) {
        //Don't call the super.  We don't need no stinkin' timestamps.
        g.write("id", this.id);
        this.ticket.toJSON(g, "ticket");
    }//jsonHelper(g)*/
}//TicketPresentation*/
