package info.serveros.messages;

import info.serveros.algorithms.*;
import java.util.Date;
import javax.json.JsonValue;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.stream.JsonGenerator;

/**
 *  A request for Credentials to Access a Provider.
 */
public class CredentialRequest extends Encryptable {

    /**
     *  The id of the application requesting the credentials.
     */
    public final JsonValue requester;

    /**
     *  The id of the application we're trying to access.
     */
    public final JsonValue requested;

    /**
     *  A Number, used Once.
     */
    public final long nonce;

    /**
     *  A Hash.
     */
    public final HashSpec hash;

    /**
     *  Hashes supported by this application.
     */
    private final HashSpec[] supportedHashes;

    /**
     *  Ciphers supported by this application.
     */
    private final CipherSpec[] supportedCiphers;

    /**
     *  Constructor.  Uses Current Timestamp.
     *
     *  @param requester THe application requesting the credentials
     *  @param requested The application we need credentials for
     *  @param nonce A random number.
     *  @param hash A Hash.
     *  @param supportedHashes the Hashes supported by this application.
     *  @param supportedCiphers the Ciphers supported by this application.
     */
    public CredentialRequest(JsonValue requester
            , JsonValue requested
            , long nonce
            , HashSpec hash
            , HashSpec[] supportedHashes
            , CipherSpec[] supportedCiphers
        ) {
        super();
        this.requester = requester;
        this.requested = requested;
        this.nonce = nonce;
        this.hash = hash;
        this.supportedHashes = supportedHashes.clone();
        this.supportedCiphers = supportedCiphers.clone();
    }//CredentialRequest(String, String, long, HashSpec, HashSpec[], CipherSpec[])*/

    /**
     *  Constructor.
     *
     *  @param requester THe application requesting the credentials
     *  @param requested The application we need credentials for
     *  @param hash A Hash.
     *  @param ts The timestamp in question.
     *  @param supportedHashes the Hashes supported by this application.
     *  @param supportedCiphers the Ciphers supported by this application.
     */
    public CredentialRequest(JsonValue requester
            , JsonValue requested
            , HashSpec hash
            , HashSpec[] supportedHashes
            , CipherSpec[] supportedCiphers
        ) {
        this(requester, requested, Encryptable.generateNonce(), hash, supportedHashes, supportedCiphers);
    }//CredentialRequest(String, String, HashSpec, HashSpec[], CipherSpec[])*/

    /**
     *  Constructor.  Uses Current Timestamp.
     *
     *  @param requester THe application requesting the credentials
     *  @param requested The application we need credentials for
     *  @param hash A Hash.
     *  @param ts The timestamp in question.
     *  @param supportedHashes the Hashes supported by this application.
     *  @param supportedCiphers the Ciphers supported by this application.
     */
    public CredentialRequest(JsonValue requester
            , JsonValue requested
            , HashSpec hash
            , Date ts
            , HashSpec[] supportedHashes
            , CipherSpec[] supportedCiphers
        ) {
        this(requester, requested, Encryptable.generateNonce(), hash, ts, supportedHashes, supportedCiphers);
    }//CredentialRequest(String, String, HashSpec, Date, HashSpec[], CipherSpec[])*/

    /**
     *  Constructor.  Uses Current Timestamp.
     *
     *  @param requester THe application requesting the credentials
     *  @param requested The application we need credentials for
     *  @param nonce A random number.
     *  @param hash A Hash.
     *  @param ts The timestamp in question.
     *  @param supportedHashes the Hashes supported by this application.
     *  @param supportedCiphers the Ciphers supported by this application.
     */
    public CredentialRequest(JsonValue requester
            , JsonValue requested
            , long nonce
            , HashSpec hash
            , Date ts
            , HashSpec[] supportedHashes
            , CipherSpec[] supportedCiphers
        ) {
        super(ts);
        this.requester = requester;
        this.requested = requested;
        this.nonce = nonce;
        this.hash = hash;
        this.supportedHashes = supportedHashes.clone();
        this.supportedCiphers = supportedCiphers.clone();
    }//CredentialRequest(String, String, long, HashSpec Date, HashSpec[], CipherSpec[])*/

    /**
     *  Constructor.  Unpacks a JSON object.
     *
     *  @param o The JSON Object representing a Credential Request.
     */
    public CredentialRequest(JsonObject o) {
        super(o);
        this.requester = o.get("requester");
        this.requested = o.get("requested");
        this.nonce = o.getJsonNumber("nonce").longValue();
        this.hash = HashSpec.fromString(o.getString("hash"));
        this.supportedHashes = CredentialRequest.getHashes(o.getJsonArray("supportedHashes"));
        this.supportedCiphers = CredentialRequest.getCiphers(o.getJsonArray("supportedCiphers"));
    }//CredentialRequest(JsonObject)*/

    /**
     *  Constructor.  Unpacks a JSON String.
     *
     *  @param s A string that represents a JSON Encoded Credential Request.
     */
    public CredentialRequest(String s) {
        this(Encryptable.unJSON(s));
    }//CredentialRequest(String)*/

    /**
     *  Get the hashes supported in this Request.
     *
     *  @return a clone of the array of supported hashes.
     */
    public HashSpec[] getSupportedHashes() {
        return (HashSpec[]) this.supportedHashes.clone();
    }//getSupportedHashes()*/

    /**
     *  Get the Ciphers supported in this request.
     *
     *  @return a clone of the array of supported Ciphers.
     */
    public CipherSpec[] getSupportedCiphers() {
        return (CipherSpec[]) this.supportedCiphers.clone();
    }//getSupportedCiphers()*/

    /**
     *  Unpack an Array of JSON strings representing Hashes.
     *
     *  @param a The array of strings.
     *
     *  @return an array of hashes.
     */
    public static HashSpec[] getHashes(JsonArray a) {
        HashSpec[] toReturn = new HashSpec[a.size()];
        for (int i = 0; i < toReturn.length; i++)
            toReturn[i] = HashSpec.fromString(a.getString(i));
        return toReturn;
    }//getHashes(JsonArray)*/

    /**
     *  Unpack an array of JSON Strings representing Ciphers.
     *
     *  @param a the array of strings.
     *
     *  @return an array of hashes
     */
    public static CipherSpec[] getCiphers(JsonArray a) {
        CipherSpec[] toReturn = new CipherSpec[a.size()];
        for (int i = 0; i < toReturn.length; i++)
            toReturn[i] = CipherSpec.fromString(a.getString(i));
        return toReturn;
    }//getCiphers(JsonArray)*//

    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected void jsonHelper(JsonGenerator g) {
        super.jsonHelper(g);
        g.write("requester", this.requester);
        g.write("requested", this.requested);
        g.write("nonce", this.nonce);
        g.write("hash", this.hash.toString());
        g.writeStartArray("supportedHashes");
        for (HashSpec h: this.supportedHashes)
            g.write(h.toString());
        g.writeEnd();
        g.writeStartArray("supportedCiphers");
        for (CipherSpec c: this.supportedCiphers)
            g.write(c.toString());
        g.writeEnd();
    }//jsonHelper(g)*/
}//CredentialRequest
