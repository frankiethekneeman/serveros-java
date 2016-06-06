package info.serveros;

import java.security.PublicKey;
import java.security.PrivateKey;
import javax.json.JsonValue;
import info.serveros.algorithms.CipherSpec;
import info.serveros.algorithms.HashSpec;
import info.serveros.messages.*;
import info.serveros.exceptions.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import javax.json.JsonObject;
import org.apache.commons.lang.ArrayUtils;

/**
 *  A Serveros Consumer - Requests Credentials.
 */
public class ServerosConsumer extends Encrypter {

    /**
     *  Some JSONable ID.
     */
    public final JsonValue id;

    /**
     *  The Host/port for the master server.
     */
    public final String masterLocation;

    /**
     *  The Public Key for the Master.
     */
    public final PublicKey masterPublicKey;

    /**
     *  The private key for this server.
     */
    public final PrivateKey myPrivateKey;

    /**
     *  The Hash currently dedicated for communication with the Master.
     */
    private HashSpec chosenHash;

    /**
     *  The Cipher currently dedicated for communication with the Master.
     */
    private CipherSpec chosenCipher;

    /**
     *  Constructor.
     *
     *  @param id The Jsonable ID of this application.
     *  @param masterLocation the host/port for the master server.
     *  @param masterPublicKey the master's Public Key.
     *  @param myPrivateKey the private key for this application.
     *  @param hashPrefs Supported Hashes, in order of preference.
     *  @param cipherPrefs supported Ciphers in order of preference.
     */
    public ServerosConsumer(JsonValue id, String masterLocation, PublicKey masterPublicKey
                , PrivateKey myPrivateKey, HashSpec[] hashPrefs, CipherSpec[] cipherPrefs
            ) {
        super(cipherPrefs, hashPrefs);
        this.id = id;
        this.masterLocation = masterLocation;
        this.masterPublicKey = masterPublicKey;
        this.myPrivateKey = myPrivateKey;
        this.chosenHash = this.hashPrefs[0];
        this.chosenCipher = this.cipherPrefs[0];
    }//ServerosConsumer(JsonValue, String, PublicKey, PrivateKey, HashSpec[] CipherSpec[])*/

    /**
     *  Constructor.
     *
     *  @param id The Jsonable ID of this application.
     *  @param masterLocation the host/port for the master server.
     *  @param masterPublicKey the master's Public Key.
     *  @param myPrivateKey the private key for this application.
     */
    public ServerosConsumer(JsonValue id, String masterLocation, PublicKey masterPublicKey, PrivateKey myPrivateKey) {
        this(id, masterLocation, masterPublicKey, myPrivateKey, null, null);
    }//ServerosConsumer(JsonValue, String, PublicKey, PrivateKey)*/

    /**
     *  Constructor.
     *
     *  @param id The Jsonable ID of this application.
     *  @param masterLocation the host/port for the master server.
     *  @param masterPublicKey the master's Public Key.
     *  @param myPrivateKey the private key for this application.
     *  @param hashPrefs Supported Hashes, in order of preference.
     *  @param cipherPrefs supported Ciphers in order of preference.
     */
    public ServerosConsumer(JsonValue id, String masterLocation, String masterPublicKey
                , String myPrivateKey, HashSpec[] hashPrefs, CipherSpec[] cipherPrefs
            )
                throws java.security.GeneralSecurityException
                    , java.io.IOException
            {
        this(id
            , masterLocation
            , Encrypter.getX509PublicKey(masterPublicKey)
            , Encrypter.getPKCS8PrivateKey(myPrivateKey)
            , hashPrefs
            , cipherPrefs
        );
    }//ServerosConsumer(JsonValue, String, String, String, HashSpect[], CipherSpec[])*/

    /**
     *  Constructor.
     *
     *  @param id The Jsonable ID of this application.
     *  @param masterLocation the host/port for the master server.
     *  @param masterPublicKey the master's Public Key.
     *  @param myPrivateKey the private key for this application.
     */
    public ServerosConsumer(JsonValue id, String masterLocation, String masterPublicKey, String myPrivateKey)
                throws java.security.GeneralSecurityException
                    , java.io.IOException
            {
        this(id, masterLocation, masterPublicKey, myPrivateKey, null, null);
    }//ServerosConsumer(JsonValue, String, String, string)*/

    /**
     *  Create a Credential Request.
     *
     *  @param requested the JSONable ID for the target Application.
     *
     *  @return a credential request, ready to be encrypted and sent of.
     */
    private CredentialRequest makeRequest(JsonValue requested) {
        return new CredentialRequest(
            this.id
            , requested
            , this.chosenHash
            , this.hashPrefs
            , this.cipherPrefs
        );
    }//makeRequest(JsonValue)*/

    /**
     *  Encrypt and Sign an Encryptable.
     *
     *  @param enc The thing to be encrypted.
     *
     *  @return The encrypted version of the message.
     */
    private CryptoMessage encryptAndSign(Encryptable enc)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
            {
        return this.encryptAndSign(
            this.masterPublicKey
            , this.myPrivateKey
            , enc
            , this.chosenCipher
            , this.chosenHash
        );
    }//encryptAndSign(Encryptable)*/

    /**
     *  Select our preferred cipher from among the available.
     *
     *  @param available the Ciphers known to be supported
     *
     *  @return the highest preference CipherSpec in the parameter.  If there is no
     *      parameter, it returns our most preferred CipherSpec.  If neither of those is possible,
     *      it returns null.
     */
    private CipherSpec selectCipher(CipherSpec[] available) {
        if (available == null)
            return this.cipherPrefs.length > 0 ? this.cipherPrefs[0] : null;
        for (CipherSpec c: this.cipherPrefs) {
            if (ArrayUtils.contains(available, c)) {
                return c;
            }
        }
        return null;
    }//selectCipher(CipherSpec[])*/

    /**
     *  Select our preferred hash from among the available.
     *
     *  @param available the Hashes known to be supported
     *
     *  @return the highest preference HashSpec in the parameter.  If there is no
     *      parameter, it returns our most preferred HashSpec.  If neither of those is possible,
     *      it returns null.
     */
    private HashSpec selectHash(HashSpec[] available) {
        if (available == null)
            return this.hashPrefs.length > 0 ? this.hashPrefs[0] : null;
        for (HashSpec h: this.hashPrefs) {
            if (ArrayUtils.contains(available, h)) {
                return h;
            }
        }
        return null;
    }//selectHash(HashSpec[])*/

    /**
     *  Get the response body from a serrver, regardless of error state.
     *
     *  @param conn The connection - having already sent and begun to receive the response.
     *
     *  @return The Body.
     *
     *  @throws javax.json.JsonException if the response body isn't JSON.
     */
    private JsonObject getResponse(HttpURLConnection conn) throws java.io.IOException {
        InputStream in = conn.getResponseCode() /100 == 2 ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line)
                .append('\n')
                ;
        }
        reader.close();
        String result = response.toString();
        return  JSONable.unJSON(result);
    }//getResponse(HttpUrlConnection)*/

    /**
     *  Requeste some credentials from the Authentication master.
     *
     *  @param requested The application we need credentials for.
     *
     *  @return the response from the authentication.
     *
     *  @throws UnsupportedCipherSpecException if there is no proper Cipher to speak with the
     *      Authenticaiton Master.
     *  @throws UnsupportedHashSpecException if there is no proper Hash to speak with the
     *      Authenticaiton Master.
     *  @throws java.security.GeneralSecurityException if any encryption error is encountered.
     *  @throws VerificationException if the Signature cannot be verified.
     *  @throws java.io.IOException if something is wrong with the machine's ability to read/write
     *      to the network.
     *  @throws StaleRequestException if the Response from the Authentication master is deemed to be stale.
     *  @throws NonceMismatchException if the Response from the Authentication master does not include the correct
     *      nonce.
     *  @throws UnrecognizedResponseCodeException if the response from the server is deemed unactionable.
     */
    private CredentialResponse makeCredentialRequest(JsonValue requested)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
                    , VerificationException
                    , java.io.IOException
                    , StaleRequestException
                    , NonceMismatchException
                    , UnrecognizedResponseCodeException
            {
        CredentialRequest creq = this.makeRequest(requested);
        HttpURLConnection conn = null;
        StringBuilder req = new StringBuilder("http://")
            .append(this.masterLocation)
            .append("/authenticate?authRequest=")
            .append(URLEncoder.encode(this.encryptAndSign(creq).toString()));
        int code = 0;
        JsonObject body = null;
        try {
            URL url = new URL(req.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            code = conn.getResponseCode();
            body = this.getResponse(conn);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        if (code / 100 == 2) {
            JsonObject obj = this.decryptAndVerify(
                this.myPrivateKey
                , this.masterPublicKey
                , new CryptoMessage(body)
            );
            CredentialResponse resp = new CredentialResponse(obj);
            if (resp.isStale()) throw new StaleRequestException(resp.getTimestamp());
            if (resp.requesterNonce != creq.nonce) throw new NonceMismatchException("Requester");
            return resp;
        } else {
            if (code == 409 || code == 490) {
                Object[] available = null;
                int i = 0;
                switch (code) {
                    case 409:
                        this.chosenCipher = this.selectCipher(CredentialRequest.getCiphers(body
                            .getJsonObject("additionalInformation")
                            .getJsonArray("supported")
                        ));
                        break;
                    case 490:
                        this.chosenHash =  this.selectHash(CredentialRequest.getHashes(body
                            .getJsonObject("additionalInformation")
                            .getJsonArray("supported")
                        ));
                        break;
                }
                return this.makeCredentialRequest(requested);
            }
            throw new UnrecognizedResponseCodeException(code);
        }
    }//makeCredentialRequest(JsonValue)*/

    /**
     *  Make a TicketId, to prove to the Service Provider that I'm forreal.
     *
     *  @param resp The credential Response from the Authenticaiton Master.
     *
     *  @return a new, unencrypted TicketId.
     */
    private TicketId getTicketId(CredentialResponse resp) {
        return new TicketId(
            resp.requester
            , resp.serverNonce
            , resp.requesterNonce
            , OneTimeCredentials.toBase64(OneTimeCredentials.getRandom(resp.credentials.cipher.block))
        );
    }//getTicketId(CredentialResponse)*/

    /**
     *  Get some working Credentials for a service.
     *
     *  @param requested the ID of the service.
     *  @param endpoint the Service Provider endpoint at which credentials should be registerd.
     *
     *  @return some credententials, registered at the service provider.
     *
     *  @throws UnsupportedCipherSpecException if there is no proper Cipher to speak with the
     *      Authenticaiton Master.
     *  @throws UnsupportedHashSpecException if there is no proper Hash to speak with the
     *      Authenticaiton Master.
     *  @throws java.security.GeneralSecurityException if any encryption error is encountered.
     *  @throws VerificationException if the Signature cannot be verified.
     *  @throws java.io.IOException if something is wrong with the machine's ability to read/write
     *      to the network.
     *  @throws StaleRequestException if the Response from the Authentication master  or Service Provider is deemed to be stale.
     *  @throws NonceMismatchException if the Response from the Authentication master  or Service Provider
     *      does not include the correct nonce.
     *  @throws UnrecognizedResponseCodeException if the response from the server is deemed unactionable.
     */
    public Credentials getCredentials(JsonValue requested, String endpoint)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
                    , VerificationException
                    , java.io.IOException
                    , StaleRequestException
                    , NonceMismatchException
                    , UnrecognizedResponseCodeException
            {
        CredentialResponse resp = this.makeCredentialRequest(requested);
        TicketId id = this.getTicketId(resp);
        String enciphered = this.encipher(id, resp.credentials);
        TicketPresentation p = new TicketPresentation(enciphered, resp.ticket);
        HttpURLConnection conn = null;
        JsonObject body = null;
        int code = 0;
        try {
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream o = conn.getOutputStream();
            p.toJSON(o);
            o.close();
            code = conn.getResponseCode();
            body = this.getResponse(conn);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        if (code / 100 == 2) {
            TicketAck ack = new TicketAck(this.decipher(body.getString("message"), new OneTimeCredentials(
                resp.credentials.getKeyString()
                , id.iv
                , resp.credentials.cipher
            )));
            if (resp.isStale()) throw new StaleRequestException(resp.getTimestamp());
            if (ack.requesterNonce != id.requesterNonce) throw new NonceMismatchException("Requester");
            if (ack.serverNonce != id.serverNonce) throw new NonceMismatchException("Server");
            if (ack.finalNonce != id.finalNonce) throw new NonceMismatchException("Final");
            return new Credentials(
                resp.requested
                , resp.id
                , resp.secret
                , resp.hash
                , resp.getExpiration()
            );
        }
        throw new UnrecognizedResponseCodeException(code);
    }//getCredentials(JsonValue, String)*/
}//ServerosConsumer*/
