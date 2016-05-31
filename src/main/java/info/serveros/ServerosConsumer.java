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

public class ServerosConsumer extends Encrypter {
    public final JsonValue id;
    public final String masterLocation;
    public final PublicKey masterPublicKey;
    public final PrivateKey myPrivateKey;
    private HashSpec chosenHash;
    private CipherSpec chosenCipher;
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
    }

    private CredentialRequest makeRequest(JsonValue requested) {
        return new CredentialRequest(
            this.id
            , requested
            , this.chosenHash
            , this.hashPrefs
            , this.cipherPrefs
        );
    }

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
    }

    private CipherSpec selectCipher(CipherSpec[] available) {
        if (available == null)
            return this.cipherPrefs[0];
        for (CipherSpec c: this.cipherPrefs) {
            if (ArrayUtils.contains(available, c))
                return c;
        }
        return null;
    }

    private HashSpec selectHash(HashSpec[] available) {
        if (available == null)
            return this.hashPrefs[0];
        for (HashSpec h: this.hashPrefs) {
            if (ArrayUtils.contains(available, h))
                return h;
        }
        return null;
    }
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
        return  JSONable.unJSON(response.toString());
    }

    private CredentialResponse makeCredentialRequest(JsonValue requested)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
                    , VerificationException
                    , java.io.IOException
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
            if (!resp.isStale() && resp.requesterNonce == creq.nonce) {
                return resp;
            }
            //throw Nonce Mismatch Exception
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
            //Throw Protocol Exception
        }
        return null;
    }

    private TicketId getTicketId(CredentialResponse resp) {
        return new TicketId(
            resp.requester
            , resp.serverNonce
            , resp.requesterNonce
            , OneTimeCredentials.toBase64(OneTimeCredentials.getRandom(resp.credentials.cipher.block))
        );
    }


    // @Temporary
    public TicketPresentation getTicketPresentation(JsonValue requested) throws Exception {
        CredentialResponse resp = this.makeCredentialRequest(requested);
        TicketId id = this.getTicketId(resp);
        String enciphered = this.encipher(id, resp.credentials);
        return new TicketPresentation(enciphered, resp.ticket);
    }
    public Credentials getCredentials(JsonValue requested, String endpoint)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
                    , VerificationException
                    , java.io.IOException
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
            if (!resp.isStale() && ack.requesterNonce == id.requesterNonce
                    && ack.serverNonce == id.serverNonce
                    && ack.finalNonce == id.finalNonce
                ) {
                return new Credentials(
                    resp.requested
                    , resp.id
                    , resp.secret
                    , resp.hash
                    , resp.getExpiration()
                );
            }
            //throw exception of sadness.
        }
        //Throw Response Exception

        return null;
    }

    //public makeCredential
}
