package info.serveros.algorithms;

import java.util.ArrayList;
import javax.crypto.Cipher;

/**
 *  An identifier for the cipher algorithm.  Not all systems will support all ciphers.
 *
 *  @author Francis J.. Van Wetering IV
 */
public enum CipherSpec {

        /**
         *  CAST cipher in CBC mode - 64 bit block, 128 bit key.
         */
        CAST_cbc("CAST/CBC/PKCS5Padding", 64, 128),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 128 bit key.
         */
        aes_128_cbc("AES/CBC/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CFB mode - 128 bit block size, 128 bit key.
         */
        aes_128_cfb("AES/CFB/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CFB1 mode - 128 bit block size, 128 bit key.
         */
        aes_128_cfb1("AES/CFB1/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CFB8 mode - 128 bit block size, 128 bit key.
         */
        aes_128_cfb8("AES/CFB8/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CTR mode - 128 bit block size, 128 bit key.
         */
        aes_128_ctr("AES/CTR/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in ECB mode - 128 bit block size, 128 bit key.
         */
        aes_128_ecb("AES/ECB/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in GCM mode - 128 bit block size, 128 bit key.
         */
        aes_128_gcm("AES/GCM/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in OFB mode - 128 bit block size, 128 bit key.
         */
        aes_128_ofb("AES/OFB/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in XTS mode - 128 bit block size, 128 bit key.
         */
        aes_128_xts("AES/XTS/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 192 bit key.
         */
        aes_192_cbc("AES/CBC/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CFB mode - 128 bit block size, 192 bit key.
         */
        aes_192_cfb("AES/CFB/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CFB1 mode - 128 bit block size, 192 bit key.
         */
        aes_192_cfb1("AES/CFB1/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CFB8 mode - 128 bit block size, 192 bit key.
         */
        aes_192_cfb8("AES/CFB8/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CTR mode - 128 bit block size, 192 bit key.
         */
        aes_192_ctr("AES/CTR/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in ECB mode - 128 bit block size, 192 bit key.
         */
        aes_192_ecb("AES/ECB/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in GCM mode - 128 bit block size, 192 bit key.
         */
        aes_192_gcm("AES/GCM/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in OFB mode - 128 bit block size, 192 bit key.
         */
        aes_192_ofb("AES/OFB/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 256 bit key.
         */
        aes_256_cbc("AES/CBC/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in CFB mode - 128 bit block size, 256 bit key.
         */
        aes_256_cfb("AES/CFB/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in CFB1 mode - 128 bit block size, 256 bit key.
         */
        aes_256_cfb1("AES/CFB1/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in CFB8 mode - 128 bit block size, 256 bit key.
         */
        aes_256_cfb8("AES/CFB8/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in CTR mode - 128 bit block size, 256 bit key.
         */
        aes_256_ctr("AES/CTR/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in ECB mode - 128 bit block size, 256 bit key.
         */
        aes_256_ecb("AES/ECB/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in GCM mode - 128 bit block size, 256 bit key.
         */
        aes_256_gcm("AES/GCM/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in OFB mode - 128 bit block size, 256 bit key.
         */
        aes_256_ofb("AES/OFB/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in XTS mode - 128 bit block size, 256 bit key.
         */
        aes_256_xts("AES/XTS/PKCS5Padding", 128, 256),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 128 bit key.
         */
        aes128("AES/CBC/PKCS5Padding", 128, 128),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 192 bit key.
         */
        aes192("AES/CBC/PKCS5Padding", 128, 192),

        /**
         *  AES cipher in CBC mode - 128 bit block size, 256 bit key.
         */
        aes256("AES/CBC/PKCS5Padding", 128, 256),

        /**
         *  BLOWFISH cipher in CBC mode - 64 bit block size, 448 bit key.
         */
        bf("BLOWFISH/CBC/PKCS5Padding", 64, 448),

        /**
         *  BLOWFISH cipher in CBC mode - 64 bit block size, 448 bit key.
         */
        bf_cbc("BLOWFISH/CBC/PKCS5Padding", 64, 448),

        /**
         *  BLOWFISH cipher in CFB mode - 64 bit block size, 448 bit key.
         */
        bf_cfb("BLOWFISH/CFB/PKCS5Padding", 64, 448),

        /**
         *  BLOWFISH cipher in ECB mode - 64 bit block size, 448 bit key.
         */
        bf_ecb("BLOWFISH/ECB/PKCS5Padding", 64, 448),

        /**
         *  BLOWFISH cipher in OFB mode - 64 bit block size, 448 bit key.
         */
        bf_ofb("BLOWFISH/OFB/PKCS5Padding", 64, 448),

        /**
         *  BLOWFISH cipher in CBC mode - 64 bit block size, 448 bit key.
         */
        blowfish("BLOWFISH/CBC/PKCS5Padding", 64, 448),

        /**
         *  CAMELLIA cipher in CBC mode - 128 bit block size, 128 bit key.
         */
        camellia_128_cbc("CAMELLIA/CBC/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in CFB mode - 128 bit block size, 128 bit key.
         */
        camellia_128_cfb("CAMELLIA/CFB/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in CFB1 mode - 128 bit block size, 128 bit key.
         */
        camellia_128_cfb1("CAMELLIA/CFB1/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in CFB8 mode - 128 bit block size, 128 bit key.
         */
        camellia_128_cfb8("CAMELLIA/CFB8/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in ECB mode - 128 bit block size, 128 bit key.
         */
        camellia_128_ecb("CAMELLIA/ECB/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in OFB mode - 128 bit block size, 128 bit key.
         */
        camellia_128_ofb("CAMELLIA/OFB/PKCS5Padding", 128, 128),

        /**
         *  CAMELLIA cipher in CBC mode - 128 bit block size, 192 bit key.
         */
        camellia_192_cbc("CAMELLIA/CBC/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in CFB mode - 128 bit block size, 192 bit key.
         */
        camellia_192_cfb("CAMELLIA/CFB/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in CFB1 mode - 128 bit block size, 192 bit key.
         */
        camellia_192_cfb1("CAMELLIA/CFB1/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in CFB8 mode - 128 bit block size, 192 bit key.
         */
        camellia_192_cfb8("CAMELLIA/CFB8/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in ECB mode - 128 bit block size, 192 bit key.
         */
        camellia_192_ecb("CAMELLIA/ECB/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in OFB mode - 128 bit block size, 192 bit key.
         */
        camellia_192_ofb("CAMELLIA/OFB/PKCS5Padding", 128, 192),

        /**
         *  CAMELLIA cipher in CBC mode - 128 bit block size, 256 bit key.
         */
        camellia_256_cbc("CAMELLIA/CBC/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher in CFB mode - 128 bit block size, 256 bit key.
         */
        camellia_256_cfb("CAMELLIA/CFB/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher in CFB1 mode - 128 bit block size, 256 bit key.
         */
        camellia_256_cfb1("CAMELLIA/CFB1/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher in CFB8 mode - 128 bit block size, 256 bit key.
         */
        camellia_256_cfb8("CAMELLIA/CFB8/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher in ECB mode - 128 bit block size, 256 bit key.
         */
        camellia_256_ecb("CAMELLIA/ECB/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher in OFB mode - 128 bit block size, 256 bit key.
         */
        camellia_256_ofb("CAMELLIA/OFB/PKCS5Padding", 128, 256),

        /**
         *  CAMELLIA cipher - 128 bit block size, 128 bit key size.
         */
        camellia128("CAMELLIA", 128, 128),

        /**
         *  CAMELLIA cipher - 128 bit block size, 192 bit key size.
         */
        camellia192("CAMELLIA", 128, 192),

        /**
         *  CAMELLIA cipher - 128 bit block size, 256 bit key size.
         */
        camellia256("CAMELLIA", 128, 256),

        /**
         *  CAST cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        cast("CAST/CBC/PKCS5Padding", 64, 128),

        /**
         *  CAST cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        cast_cbc("CAST/CBC/PKCS5Padding", 64, 128),

        /**
         *  CAST5 cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        cast5_cbc("CAST5/CBC/PKCS5Padding", 64, 128),

        /**
         *  CAST5 cipher in CFB mode - 64 bit block size, 128 bit key.
         */
        cast5_cfb("CAST5/CFB/PKCS5Padding", 64, 128),

        /**
         *  CAST5 cipher in ECB mode - 64 bit block size, 128 bit key.
         */
        cast5_ecb("CAST5/ECB/PKCS5Padding", 64, 128),

        /**
         *  CAST5 cipher in OFB mode - 64 bit block size, 128 bit key.
         */
        cast5_ofb("CAST5/OFB/PKCS5Padding", 64, 128),

        /**
         *  DES cipher in CBC mode - 64 bit block size, 56 bit key
         */
        des("DES/CBC/PKCS5Padding", 64, 56),

        /**
         *  DES cipher in CBC mode - 64 bit block size, 56 bit key.
         */
        des_cbc("DES/CBC/PKCS5Padding", 64, 56),

        /**
         *  DES cipher in CFB mode - 64 bit block size, 56 bit key.
         */
        des_cfb("DES/CFB/PKCS5Padding", 64, 56),

        /**
         *  DES cipher in CFB1 mode - 64 bit block size, 56 bit key.
         */
        des_cfb1("DES/CFB1/PKCS5Padding", 64, 56),

        /**
         *  DES cipher in CFB8 mode - 64 bit block size, 56 bit key.
         */
        des_cfb8("DES/CFB8/PKCS5Padding", 64, 56),

        /**
         *  DES cipher in ECB mode - 64 bit block size, 56 bit key.
         */
        des_ecb("DES/ECB/PKCS5Padding", 64, 56),

        /**
         *  DESede cipher in ECB mode - 64 bit block size, 112 bit key.
         */
        des_ede("DESede/ECB/PKCS5Padding", 64, 112),

        /**
         *  DESede cipher in CBC mode - 64 bit block size, 112 bit key.
         */
        des_ede_cbc("DESede/CBC/PKCS5Padding", 64, 112),

        /**
         *  DESede cipher in CFB mode - 64 bit block size, 112 bit key.
         */
        des_ede_cfb("DESede/CFB/PKCS5Padding", 64, 112),

        /**
         *  DESede cipher in OFB mode - 64 bit block size, 112 bit key.
         */
        des_ede_ofb("DESede/OFB/PKCS5Padding", 64, 112),

        /**
         *  DESede3 cipher in ECB mode - 64 bit block size, 168 bit key.
         */
        des_ede3("DESede3/ECB/PKCS5Padding", 64, 168),

        /**
         *  DESede3 cipher in CBC mode - 64 bit block size, 168 bit key.
         */
        des_ede3_cbc("DESede3/CBC/PKCS5Padding", 64, 168),

        /**
         *  DESede3 cipher in CFB mode - 64 bit block size, 168 bit key.
         */
        des_ede3_cfb("DESede3/CFB/PKCS5Padding", 64, 168),

        /**
         *  DESede3 cipher in OFB mode - 64 bit block size, 168 bit key.
         */
        des_ede3_ofb("DESede3/OFB/PKCS5Padding", 64, 168),

        /**
         *  DES cipher in OFB mode - 64 bit block size, 56 bit key.
         */
        des_ofb("DES/OFB/PKCS5Padding", 64, 56),

        /**
         *  DESede3 cipher in CBC mode - 64 bit block size, 168 bit key.
         */
        des3("DESede3/CBC/PKCS5Padding", 64, 168),

        /**
         *  DESX cipher in CBC mode - 64 bit block size, 184 bit key.
         */
        desx("DESX/CBC/PKCS5Padding", 64, 184),

        /**
         *  DESX cipher in CBC mode - 64 bit block size, 184 bit key.
         */
        desx_cbc("DESX/CBC/PKCS5Padding", 64, 184),

        /**
         *  ID cipher in AES128_GCM mode - 128 bit block size, 128 bit key.
         */
        id_aes128_GCM("ID/AES128_GCM/PKCS5Padding", 128, 128),

        /**
         *  ID cipher in AES192_GCM mode - 128 bit block size, 192 bit key.
         */
        id_aes192_GCM("ID/AES192_GCM/PKCS5Padding", 128, 192),

        /**
         *  ID cipher in AES256_GCM mode - 128 bit block size, 256 bit key.
         */
        id_aes256_GCM("ID/AES256_GCM/PKCS5Padding", 128, 256),

        /**
         *  IDEA cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        idea("IDEA/CBC/PKCS5Padding", 64, 128),

        /**
         *  IDEA cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        idea_cbc("IDEA/CBC/PKCS5Padding", 64, 128),

        /**
         *  IDEA cipher in CFB mode - 64 bit block size, 128 bit key.
         */
        idea_cfb("IDEA/CFB/PKCS5Padding", 64, 128),

        /**
         *  IDEA cipher in ECB mode - 64 bit block size, 128 bit key.
         */
        idea_ecb("IDEA/ECB/PKCS5Padding", 64, 128),

        /**
         *  IDEA cipher in OFB mode - 64 bit block size, 128 bit key.
         */
        idea_ofb("IDEA/OFB/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        rc2("RC2/CBC/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        rc2_cbc("RC2/CBC/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in CFB mode - 64 bit block size, 128 bit key.
         */
        rc2_cfb("RC2/CFB/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in ECB mode - 64 bit block size, 128 bit key.
         */
        rc2_ecb("RC2/ECB/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in OFB mode - 64 bit block size, 128 bit key.
         */
        rc2_ofb("RC2/OFB/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        rc2_40_cbc("RC2/CBC/PKCS5Padding", 64, 128),

        /**
         *  RC2 cipher in CBC mode - 64 bit block size, 128 bit key.
         */
        rc2_64_cbc("RC2/CBC/PKCS5Padding", 64, 128),

        /**
         *  SEED cipher in CBC mode - 128 bit block size, 128 bit key.
         */
        seed("SEED/CBC/PKCS5Padding", 128, 128),

        /**
         *  SEED cipher in CBC mode - 128 bit block size, 128 bit key.
         */
        seed_cbc("SEED/CBC/PKCS5Padding", 128, 128),

        /**
         *  SEED cipher in CFB mode - 128 bit block size, 128 bit key.
         */
        seed_cfb("SEED/CFB/PKCS5Padding", 128, 128),

        /**
         *  SEED cipher in ECB mode - 128 bit block size, 128 bit key.
         */
        seed_ecb("SEED/ECB/PKCS5Padding", 128, 128),

        /**
         *  SEED cipher in OFB mode - 128 bit block size, 128 bit key.
         */
        seed_ofb("SEED/OFB/PKCS5Padding", 128, 128)
        ;

    /**
     *  The string to be passed into Cipher.getInstance to begin encryption.
     */
    public final String cipherSpec;

    /**
     *  The block size of the cipher - in bits.
     */
    public final int block;

    /**
     *  The size of the key in bits.
     */
    public final int key;

    /**
     *  Create a new CipherSpec.
     *
     *  @param cipherSpec The string to be passed into Cipher.getInstance
     *  @param block the Block size in bits.
     *  @param key the key size in bits.
     */
    CipherSpec(String cipherSpec, int block, int key) {
        this.cipherSpec = cipherSpec;
        this.block = block;
        this.key = key;
    }//CipherSpec(String, int, int)*/

    /**
     *  Change to a string for output reasons.
     *
     *  @return the name of this algorithm, with underscores changed to hyphens.
     */
    public String toString() {
        return this.name().replace('_', '-');
    }//toString()*/

    /**
     *  Get an algorithm from its string value.
     *
     *  @param str The string name.
     *
     *  @return A copy of the algorithm.
     */
    public static CipherSpec fromString(String str) {
        return CipherSpec.valueOf(str.replace('-', '_'));
    }//fromString(String)*/

    /**
     *  Get a list of all algorithms supported by the current JVM. Identical to calling CipherSpec.filter(null)
     *
     *  @return all supported CipherSpecs.
     */
    public static CipherSpec[] filter() {
        return CipherSpec.filter(null);
    }//filter()*/

    /**
     *  Get a Cipher Instance to actually encipher things.
     */
    public Cipher getInstance() throws java.security.NoSuchAlgorithmException, javax.crypto.NoSuchPaddingException {
        return Cipher.getInstance(this.cipherSpec);
    }//getInstance()*/

    /**
     *  Get the name of this algorithm.
     */
    public String getAlgorithmName() {
        return this.cipherSpec.split("/")[0];
    }//getAlgorithmName()*/

    /**
     *  Filter the list of given algorithms, returning only those supported by the JVM.
     *  If the argument is null, then start with all available algorithms.
     *
     *  @param source an array of acceptable algorithms, in order of preference.
     *  @return of the passed algorithms, only the ones supported by this system.
     */
    public static CipherSpec[] filter(CipherSpec[] source) {
        if (source == null)
            source = CipherSpec.values();

        ArrayList<CipherSpec> toReturn = new ArrayList<>();
        for (CipherSpec a: source) {
            try {
                Cipher c = Cipher.getInstance(a.cipherSpec);
                if (c.getBlockSize() * 8 != a.block) {
                    System.out.println(a.cipherSpec + " block size diff: " + c.getBlockSize() + "/" + a.block);
                    continue;
                }
                if (Cipher.getMaxAllowedKeyLength(a.cipherSpec) < a.key) {
                    System.out.println(a.cipherSpec + " key size diff: " + Cipher.getMaxAllowedKeyLength(a.cipherSpec) + "/" + a.key);
                    continue;
                }
                toReturn.add(a);
            } catch (java.security.GeneralSecurityException e) {
                //Suppressing the exception cause frankly.
            }
        }
        return toReturn.toArray(new CipherSpec[toReturn.size()]);
    }//filter(CipherSpec[])*/

}//CipherSpec*/
