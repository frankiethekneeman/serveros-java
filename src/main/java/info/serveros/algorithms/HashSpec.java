package info.serveros.algorithms;

import java.util.ArrayList;
import java.security.Signature;


/**
 *  An identifier for the cipher algorithm.  Not all systems will support all ciphers.
 *
 *  @author Francis J.. Van Wetering IV
 */
public enum HashSpec {

        /**
         *  The MD5 Hash
         */
        md5("MD5withRSA"),

        /**
         *  The SHA hash.
         */
        sha("SHAwithRSA"),

        /**
         *  The SHA 1 hash
         */
        sha1("SHA1withRSA"),

        /**
         *  The SHA Hash with a 224 bit result.
         */
        sha224("SHA224withRSA"),

        /**
         *  The SHA Hash with a 256 bit result.
         */
        sha256("SHA256withRSA"),

        /**
         *  The SHA hash with a 384 bit result.
         */
        sha384("SHA384withRSA"),

        /**
         *  The SHA hash with a 512 bit result.
         */
        sha512("SHA512withRSA")
        ;

    /**
     *  The string to be passed into MessageDigest.getInstance to begin hashing.
     */
    public final String hashName;

    /**
     *  Create a new HashSpec.
     *
     *  @param cipherSpec The string to be passed into MessageDigest.getInstance
     *  @param block the Block size in bits.
     *  @param key the key size in bits.
     */
    HashSpec(String hashName) {
        this.hashName = hashName;
    }//HashSpec(String)*/

    /**
     *  Change to a string for output reasons.
     *
     *  @return the name of this algorithm, with underscores changed to hyphens.
     */
    public String toString() {
        return this.name().replace('_', '-');
    }//toString()*/

    /**
     *  Get a HashSpec from its string value.
     *
     *  @param str The string name.
     *
     *  @return A copy of the hash.
     */
    public static HashSpec fromString(String str) {
        return HashSpec.valueOf(str.replace('-', '_'));
    }//fromString(String)*/

    /**
     *  Get a list of all algorithms supported by the current JVM. Identical to calling HashSpec.filter(null)
     *
     *  @return all supported HashSpecs.
     */
    public static HashSpec[] filter() {
        return HashSpec.filter(null);
    }//filter()*/

    /**
     *  Get a Cipher Instance to actually encipher things.
     */
    public Signature getInstance() throws java.security.NoSuchAlgorithmException, javax.crypto.NoSuchPaddingException {
        return Signature.getInstance(this.hashName);
    }//getInstance()*/


    /**
     *  Filter the list of given algorithms, returning only those supported by the JVM.
     *  If the argument is null, then start with all available algorithms.
     *
     *  @param source an array of acceptable algorithms, in order of preference.
     *  @return of the passed algorithms, only the ones supported by this system.
     */
    public static HashSpec[] filter(HashSpec[] source) {
        if (source == null)
            source = HashSpec.values();

        ArrayList<HashSpec> toReturn = new ArrayList<>();
        for (HashSpec h: source) {
            try {
                Signature s = Signature.getInstance(h.hashName);
                toReturn.add(h);
            } catch (java.security.GeneralSecurityException e) {
                //Suppressing the exception cause frankly.
            }
        }
        return toReturn.toArray(new HashSpec[toReturn.size()]);
    }//Filter(HashSpec[])*/

}//HashSpec*/
