package info.serveros;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.json.stream.JsonGenerator;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import java.io.StringWriter;
import java.io.StringReader;
import info.serveros.algorithms.CipherSpec;
import info.serveros.algorithms.HashSpec;
import info.serveros.messages.Encryptable;

/**
 *  One time credentials - for symmetric encryption.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class OneTimeCredentials extends JSONable {

    /**
     *  The byte array used as a key.
     */
    private final byte[] key;

    /**
     *  The byte array used as an initial vector.
     */
    private final byte[] iv;

    /**
     *  CipherSpec being used here.
     */
    public final CipherSpec cipher;

    /**
     *  HashSpec being used.
     */
    public final HashSpec hash;

    /**
     *  Default SecureRandom for the calss.
     */
    private static final SecureRandom generator = new SecureRandom();

    /**
     *  Constructor with prebuilt credentials as byte arrays.
     *
     *  @param key The key bytes.
     *  @param iv the Initial Vector bytes.
     *  @param cipher The CipherSpec algorithm to use.
     *  @param hash The HashSpec algorithm to use.
     */
    public OneTimeCredentials(byte[] key, byte[] iv, CipherSpec cipher, HashSpec hash) {
        this.key = key;
        this.iv = iv;
        this.cipher = cipher;
        this.hash = hash;
    }//OneTimeCredentials(byte[], byte[], CipherSpec)*/

    /**
     *  Constructor with prebuilt credentials as byte arrays.
     *
     *  @param key The key bytes.
     *  @param iv the Initial Vector bytes.
     *  @param cipher The CipherSpec algorithm to use.
     */
    public OneTimeCredentials(byte[] key, byte[] iv, CipherSpec cipher) {
        this(key, iv, cipher, null);
    }//OneTimeCredentials(byte[], byte[], CipherSpec)*/

    /**
     *  Constructor with prebuilt credentials in base64 string.
     *
     *  @param key the key in base64
     *  @param iv the Initial Vector, in base64.
     *  @param cipher the CipherSpec algorithm to use.
     */
    public OneTimeCredentials(String key, String iv, CipherSpec cipher) {
        this(OneTimeCredentials.fromBase64(key), OneTimeCredentials.fromBase64(iv), cipher, null);
    }//OneTimeCredentials(String, String, CipherSpec)*/

    /**
     *  Constructor with prebuilt credentials in base64 string.
     *
     *  @param key the key in base64
     *  @param iv the Initial Vector, in base64.
     *  @param cipher the CipherSpec algorithm to use.
     *  @param hash The hash algorithm assosciated with these credentials.
     */
    public OneTimeCredentials(String key, String iv, CipherSpec cipher, HashSpec hash) {
        this(OneTimeCredentials.fromBase64(key), OneTimeCredentials.fromBase64(iv), cipher, hash);
    }//OneTimeCredentials(String, String, CipherSpec)*/

    /**
     *  Constructor - generates new keys.
     *
     *  @param cipher The algorithm to use. - uses a default SecureRandom
     */
    public OneTimeCredentials(CipherSpec cipher) {
        this(cipher, OneTimeCredentials.generator);
    }//OneTimeCredentials(CipherSpec)*/

    /**
     *  Constructor - generates new keys.
     *
     *  @param cipher the algorithm to use.
     *  @param randomness a source of entropy to generate the keys from.
     */
    public OneTimeCredentials(CipherSpec cipher, Random randomness) {
        this(OneTimeCredentials.getRandom(cipher.key, randomness), OneTimeCredentials.getRandom(cipher.block, randomness), cipher);
    }//OneTimeCredentials(CipherSpec, Random)*/
    /**
     *  Constructor - reads in from JSON.
     *
     *  @param json The JSON format of the credentials passed.
     */
    public OneTimeCredentials(String json) {
        this(Encryptable.unJSON(json));
    }//OneTimeCredentials(String)*/
    public OneTimeCredentials(JsonObject obj) {
        this(
            obj.getString("key")
            , obj.getString("iv")
            , obj.containsKey("cipher") ?
                CipherSpec.fromString(obj.getString("cipher"))
                :
                CipherSpec.fromString(obj.getString("algorithm"))
            , (obj.containsKey("hash")) ?
                HashSpec.fromString(obj.getString("hash"))
                :
                null
        );
    }

    /**
     *  Get get random bits. Gets at most the passed number of bits of randomness.
     *
     *  @param bits bits of randomness requested.
     *
     *  @return A number of bytes not greater than the requested number of bits divided by eight
     */
    public static byte[] getRandom(int bits) {
        return OneTimeCredentials.getRandom(bits, OneTimeCredentials.generator);
    }//getRandom(int)*/

    /**
     *  Get get random bits. Gets at most the passed number of bits of randomness.
     *
     *  @param bits bits of randomness requested.
     *  @param randomness a custom source of randomness.
     *
     *  @return A number of bytes not greater than the requested number of bits divided by eight
     */
    public static byte[] getRandom(int bits, Random randomness) {
        byte[] toReturn = new byte[bits/8];
        randomness.nextBytes(toReturn);
        return toReturn;
    }//getRandom(int, Random)*/

    /**
     *  Get the key as a base64 string.
     *
     *  @return The key encoded as base64
     */
    public String getKeyString() {
        return OneTimeCredentials.toBase64(this.key);
    }//getKeyString()*/

    /**
     *  Get the IV as a base64 string.
     *
     *  @return the IV encoded as base64
     */
    public String getIVString() {
        return OneTimeCredentials.toBase64(this.iv);
    }//getIVString()*/

    /**
     *  Get the key as a SecretKeySpec.
     *
     *  @return a secret key useable with javax.crypto.Cipher
     */
    public SecretKeySpec getKey() {
        return new SecretKeySpec(this.key.clone(), this.cipher.getAlgorithmName());
    }//getKey()*/

    /**
     *  Get the IV as an IVParametrSpec
     *
     *  @return The IV suitable for use with javax.crypto.Cipher
     */
    public IvParameterSpec getIV() {
        return new IvParameterSpec(this.iv.clone());
    }//getIV()*/


    /**
     *  Turn this object into JSON.
     *
     *  @param g The JSON Generator to output to.
     *  @param name The name to use for the object - or null to just output an object.
     */
    @Override
    public void jsonHelper(JsonGenerator g) {
        g.write("cipher", this.cipher.toString())
            .write("algorithm", this.cipher.toString())
            .write("key", this.getKeyString())
            .write("iv", this.getIVString())
            ;
        if (this.hash != null)
            g.write("hash", this.hash.toString());
    }

    /**
     *  Bytes to base64.
     *
     *  @param bytes the Bytes.
     *
     *  @return a base64 encoded string.
     */
    public static String toBase64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }//toBase64(byte[])*/

    /**
     *  Base64 to bytes.
     *
     *  @param base64 The string
     *
     *  @return the byte array represented by the input.
     */
    public static byte[] fromBase64(String base64) {
        return DatatypeConverter.parseBase64Binary(base64);
    }//fromBase64(String)*/


}//OneTimeCredentials*/
