package info.serveros;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import info.serveros.exceptions.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.Security;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import info.serveros.algorithms.CipherSpec;
import info.serveros.algorithms.HashSpec;
import info.serveros.messages.*;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 *  Base Encrypter class with Encryption powers!
 *
 *  @author Francis J.. Van Wetering IV
 */
public class Encrypter {

    /**
     * The delimiter used in Cryptexts.
     */
    public static final String DELIMITER = ":";

    /**
     * Clock Drift.
     */
    public static final int STALE_REQUEST_TOLERANCE = 60000; // One minute clock drift allowed.

    /**
     * A Regular Expression to strip Padding Characters.
     */
    public static final String PADDING_CHARACTERS = "(?:([\\x00-\\x1F])\\1*|\\x80\\x00*|\\x00*[\\x01-\\x1F])$";

    /**
     * The Preferred CipherSpecs, in order.
     */
    protected CipherSpec[] cipherPrefs;

    /**
     * The preferred HashSpeces, in Order.
     */
    protected HashSpec[] hashPrefs;

    /**
     *  Constructor
     *
     *  @param cipherPrefs Acceptable CipherSpecs, in order of preference.
     *  @param hashPrefs Acceptable hash, in order of preference.
     */
    public Encrypter(CipherSpec[] cipherPrefs, HashSpec[] hashPrefs) {
        this.cipherPrefs = CipherSpec.filter(cipherPrefs);
        this.hashPrefs = HashSpec.filter(hashPrefs);
    }//Encrypter(CipherSpec[], HashSpec[])*/

    /**
     *  Constructor.  Defaults to all available CipherSpecs and HashSpeces.
     */
    public Encrypter() {
        this(null, null);
    }//Encrypter()*/

    /**
     *  Get the prefered ciphers for the encrypters.
     *
     *  @return a clone of the cipherPrefs member.
     */
    private CipherSpec[] cipherPrefs() {
        return this.cipherPrefs.clone();
    }//cipherPrefs()*/

    /**
     *  Get the prefered hashes for the encrypter.
     *
     *  @return a clone of the hashPrefs member.
     */
    private HashSpec[] hashPrefs() {
        return this.hashPrefs.clone();
    }//hashPrefs*/

    /**
     *  Generate One Time credentials - if and only if the cipher requested is valid.
     *
     *  @param cipher The CipherSpec algorithm that
     *
     *  @return Some cryptographically secure one-time credentials for you to use
     *
     *  @throws UnsupportedCipherException if the requested cipher is not allowed for this Encrypter
     */
    public OneTimeCredentials getOneTimeCredentials(CipherSpec cipher)
            throws UnsupportedCipherSpecException {
        int i = 0;
        for (i = 0; i < this.cipherPrefs.length; i++) {
            if (cipher == this.cipherPrefs[i])
                break;
        }
        if (i == this.cipherPrefs.length)
            throw new UnsupportedCipherSpecException(cipher, this.cipherPrefs.clone());
        return new OneTimeCredentials(cipher);
    }//getOneTimeCredentials(CipherSpec)*/

    /**
     *  Encipher a message to a base64 string.
     *
     *  @param message The message is
     *  @param credentials The credentials to use to encipher the message.
     *
     *  @return the base64 encoded bytes of the enciphered message.
     *
     *  @throws GeneralSecurityException if something goes wrong enciphering.
     */
    public String encipher(String message, OneTimeCredentials credentials)
                throws java.security.GeneralSecurityException
            {
        Cipher c = credentials.cipher.getInstance();
        c.init(Cipher.ENCRYPT_MODE, credentials.getKey(), credentials.getIV());
        return OneTimeCredentials.toBase64(c.doFinal(message.getBytes()));
    }//encipher(String, OneTimeCredentials)*/

    public String encipher(Object message, OneTimeCredentials credentials)
                throws java.security.GeneralSecurityException
            {
        return this.encipher(message.toString(), credentials);
    }//encipher(String, OneTimeCredentials)*/


    /**
     *  Decipher the base64 string back to a string message.
     *
     *  @param bytes the base64 string.
     *  @param credentialse the credentials originally used to encipher the string.
     *
     *  @return The deciphered message.
     *
     *  @throws GeneralSecurityException if something goes wrong deciphering.
     */
    public JsonObject decipher(String bytes, OneTimeCredentials credentials)
            throws java.security.GeneralSecurityException
            {
        Cipher c = credentials.cipher.getInstance();
        c.init(Cipher.DECRYPT_MODE, credentials.getKey(), credentials.getIV());
        return JSONable.unJSON(new String(c.doFinal(OneTimeCredentials.fromBase64(bytes))));
    }//decipher(String, OneTimeCredentials)*/

    /**
     *  Encrypt a message with an RSA key.
     *
     *  @param rsaKey the key to use.
     *  @param message the message to encrypt.
     *  @param cipher the CipherSpec algorithm to use.
     *
     *  @return the encrypted message.
     *
     *  @throws UnsupportedCipherSpecException if the requested cipher is not allowed for this Encrypter
     *  @throws GeneralSecurityException if something goes wrong deciphering.
     */
    public String encrypt(Key rsaKey, Object message, CipherSpec cipher)
                throws UnsupportedCipherSpecException
                    , java.security.GeneralSecurityException
            {
        OneTimeCredentials credentials = this.getOneTimeCredentials(cipher);
        String enciphered = this.encipher(message, credentials);
        String lock = credentials.toJSON();
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        c.init(Cipher.ENCRYPT_MODE, rsaKey);
        return enciphered + Encrypter.DELIMITER + OneTimeCredentials.toBase64(c.doFinal(credentials.toJSON().getBytes()));
    }//encrypt(Key, String, CipherSpec)*/

    /**
     *  Decrypt the message encrypted with the encrypt method.
     *
     *  @param rsaKey the key to decrypt with.
     *  @param encrypted the String of encrypted information.
     *
     *  @return the decrypted data.
     *
     *  @throws GeneralSecurityException if something goes wrong deciphering.
     */
    public JsonObject decrypt(Key rsaKey, String encrypted)
                throws java.security.GeneralSecurityException
            {
        String[] pieces = encrypted.split(Encrypter.DELIMITER);
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        c.init(Cipher.DECRYPT_MODE, rsaKey);
        OneTimeCredentials credentials = new OneTimeCredentials(new String(c.doFinal(OneTimeCredentials.fromBase64(pieces[1]))));
        return this.decipher(pieces[0], credentials);
    }//decrypt(Key, String)*/

    /**
     *  Sign some data.
     *
     *  @param rsaKey the private key to sign with.
     *  @param data the data to sign.
     *  @param algorithm The hash algorithm to use.
     *
     *  @return the base64 encoded signature.
     *
     *  @throws UnsupportedHashSpecException if the requested hash is not supported by the Encrypter.
     *  @throws GeneralSecurityException if something goes wrong deciphering.
     */
    public String sign(PrivateKey rsaKey, String data, HashSpec algorithm)
                throws java.security.GeneralSecurityException
                    , UnsupportedHashSpecException
            {
        int i = 0;
        for (i = 0; i < this.hashPrefs.length; i++) {
            if (algorithm == this.hashPrefs[i])
                break;
        }
        if (i == this.hashPrefs.length)
            throw new UnsupportedHashSpecException(algorithm, this.hashPrefs.clone());
        Signature s = algorithm.getInstance();
        s.initSign(rsaKey);
        s.update(data.getBytes());
        return OneTimeCredentials.toBase64(s.sign());
    }//sign(PrivateKey, String, HashSpec)*/

    /**
     *  Verify a signature.
     *
     *  @param rsaKey the public key to verify the signature against.
     *  @param data the data that was signed.
     *  @param algorithm the hash used to sign the data.
     *  @param signature the base64 signature.
     *
     *  @return true if the signatures match, false otherwise.
     *
     *  @throws GeneralSecurityException if something goes wrong deciphering.
     */
    public boolean verify(PublicKey rsaKey, String data, HashSpec algorithm, String signature)
                throws java.security.GeneralSecurityException
            {
        Signature s = algorithm.getInstance();
        s.initVerify(rsaKey);
        s.update(data.getBytes());
        return s.verify(OneTimeCredentials.fromBase64(signature));
    }//verify(PublicKey, String, HashSpec, String)*/

    /**
     *  Decide if a timestamp is stale.
     *
     *  @param ts The timestamp in question.
     *
     *  @return true if the timestamp is more that STALE_REQUEST_TOLERANCE away from the current timestamp.
     */
    public boolean isStale(Date ts) {
        if (ts == null)
            return true;
        return this.isStale(ts.getTime());
    }//isStale(Date)*/

    /**
     *  Decide if a timestamp is stale.
     *
     *  @param ts in milliseconds since the epoch.
     *
     *  @return true if the timestamp is more that STALE_REQUEST_TOLERANCE away from the current timestamp.
     */
    public boolean isStale(long ts) {
        return Math.abs(System.currentTimeMillis() - ts) > Encrypter.STALE_REQUEST_TOLERANCE;
    }//isStale(long)*/

    /**
     *  Encrypt and sign a message.
     *
     *  @param encryptKey The RSA key to encrypt with.
     *  @param signKey The RSA key to sign with.
     *  @param message the Message to encrypt.
     *  @param cipher the cipher to use.
     *  @param hash the HashSpec to use.
     *
     *  @return a message, signature, and hash object that can be decrypted later.
     *
     *  @throws UnsupportedCipherSpecException if the requested cipher is not allowed for this Encrypter
     *  @throws UnsupportedHashSpecException if the requested hash is not supported by the Encrypter.
     *  @throws GeneralSecurityException if something goes wrong encrypting or signing.
     */
    public CryptoMessage encryptAndSign(Key encryptKey, PrivateKey signKey, Object message, CipherSpec cipher, HashSpec hash)
                throws UnsupportedCipherSpecException
                    , UnsupportedHashSpecException
                    , java.security.GeneralSecurityException
            {
        String encrypted = this.encrypt(encryptKey, message, cipher);
        String signed = this.sign(signKey, encrypted, hash);
        return new CryptoMessage(encrypted, signed, hash);
    }//encryptAndSign(Key, PrivateKey, String, CipherSpec, HashSpec)*/


    /**
     *  Decrypt and verify a message.
     *
     *  @param decryptKey the RSA key to decrypt with.
     *  @param verifyKey the RSA key to verify the signature with.
     *  @param message the Encrypted/signed message (with the hash signature algorithm).
     *
     *  @return the decrypted message, if and only if the signature checks out.
     *
     *  @throws GeneralSecurityException if something goes wrong decrypting or verifying the signature.
     *  @throws VerificationException if the signature is not verified.
     */
    public JsonObject decryptAndVerify(Key decryptKey, PublicKey verifyKey, CryptoMessage message)
                throws java.security.GeneralSecurityException
                    , VerificationException
            {
        JsonObject decrypted = this.decrypt(decryptKey, message.message);
        if (!this.verify(verifyKey, message.message, HashSpec.fromString(decrypted.getString("hash")), message.signature)) {
            throw new VerificationException();
        }
        return decrypted;
    }//decryptAndVerify(Key, PublicKey, CryptoMessage)*/

    public static class CryptoMessage extends JSONable {
        public final String message;
        public final String signature;
        public final HashSpec hash;
        public CryptoMessage(String message, String signature, HashSpec hash) {
            this.message = message;
            this.signature = signature;
            this.hash = hash;
        }

        public CryptoMessage(JsonObject o) {
            this(o.getString("message")
                , o.getString("signature")
                , o.get("hash") == null ? null : HashSpec.fromString(o.getString("hash"))
            );
        }

        public CryptoMessage(String s) {
            this(JSONable.unJSON(s));
        }

        /**
         *  Turn this object into JSON.
         *
         *  @param g The JSON Generator to output to.
         *  @param name The name to use for the object - or null to just output an object.
         */
        public void jsonHelper(JsonGenerator g) {
            g.write("message", this.message);
            g.write("signature", this.signature);
            if (this.hash != null)
                g.write("hash", this.hash.toString());
        }//toJSON(JsonGenerator, String)*/

    }

    public static void main(String[] args) throws Exception{
        Encrypter e = new Encrypter();/*new CipherSpec[]{CipherSpec.aes_256_cbc, CipherSpec.camellia_192_ecb}//, null);/*
            , new HashSpec[]{HashSpec.sha256, HashSpec.sha224, HashSpec.md5});//*/
/*

        for (HashSpec h: e.hashPrefs) {
            System.out.println(h.toString());
        }
        System.out.println();

        for (CipherSpec c: e.cipherPrefs) {
            System.out.println(c.toString());
        }
        System.out.println();

        KeyFactory f = KeyFactory.getInstance("RSA");
        PublicKey pub = getX509PublicKey("../serveros/demo/keys/master.pem8");
        PrivateKey priv = getPKCS8PrivateKey("../serveros/demo/keys/master.pkcs8");

        for (int i = 0; i < 10; i++) {

            CryptoMessage m = e.encryptAndSign(pub, priv, "MESSAGE", CipherSpec.aes_256_cbc, HashSpec.md5);
            System.out.println("MESSAGE");
            System.out.println("ENCRYPTED:" + m.message);
            System.out.println("DECRYPTED: " + e.decryptAndVerify(priv, pub, m));
            System.out.println();
        }*/
        JsonObject ids = JSONable.unJSON("{\"me\":\"Application A\", \"them\": \"Application B\"}");
        ServerosConsumer c = new ServerosConsumer(ids.getJsonString("me")
            , "127.0.0.1:3500"
            , getX509PublicKey("../serveros/demo/keys/master.pem8")
            , getPKCS8PrivateKey("../serveros/demo/keys/serverA.pkcs8")
            , null, null
        );
        ServerosProvider p = new ServerosProvider(ids.getJsonString("them")
            , getX509PublicKey("../serveros/demo/keys/master.pem8")
            , getPKCS8PrivateKey("../serveros/demo/keys/serverB.pkcs8")
            , null, null
        );
        TicketPresentation tp = c.getTicketPresentation(ids.getJsonString("them"));
        System.out.println(p.getEncipheredAck(tp));
        System.out.println(p.getCredentials(tp));

    }

    /**
     *  Read a public key in from a PEM X509 encoded KeyFile.
     *
     *  @param filename the file that holds the Public key.
     *
     *  @return the PublicKey in question.
     *
     *  @throws IOException if the file does not exist, or cannot be read.
     *  @throws GeneralSecurityException if RSA is not supported, or the key is malformed.
     */
    public static PublicKey getX509PublicKey(String filename)
                throws java.security.GeneralSecurityException
                    , java.io.IOException
            {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getKeyData(filename)));
    }//getX509PublicKey(String)*/

    /**
     *  Read a private key in from a PKCS8 Encoded key file.
     *
     *  @param filename the file that holds the Private key.
     *
     *  @return the PrivateKey in question.
     *
     *  @throws IOException if the file does not exist, or cannot be read.
     *  @throws GeneralSecurityException if RSA is not supported, or the key is malformed.
     */
    public static PrivateKey getPKCS8PrivateKey(String filename)
                throws java.security.GeneralSecurityException
                    , java.io.IOException
            {
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(getKeyData(filename)));
    }//getPKCS8PrivateKey(String)*/

    /**
     *  Get the base64 keydata from a file.
     *
     *  @param filename the file with a key in it.
     *
     *  @return the decoded bytes.
     *
     *  @throws IOException if the file does not exist, or cannot be read.
     */
    public static byte[] getKeyData(String filename)
                throws java.io.IOException
            {
        BufferedReader in = new BufferedReader ( new InputStreamReader(new FileInputStream(new File(filename))));
        String line = null;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while ( ( line = in.readLine()) != null) {
            //System.out.println(line);
            if (line.length() > 0 && line.charAt(0) != '-')
                sb.append(line);
        }
        in.close();
        return OneTimeCredentials.fromBase64(sb.toString());
    }//getKeyData(String)*/
}
