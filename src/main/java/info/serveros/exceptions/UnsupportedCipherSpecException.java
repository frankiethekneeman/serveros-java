package info.serveros.exceptions;

import info.serveros.algorithms.CipherSpec;
import javax.json.stream.JsonGenerator;

/**
 *  Thrown when an unsupported CipherSpec is encountered.
 */
public class UnsupportedCipherSpecException extends UnsupportedException {

    /**
     *  Constructor.
     *
     *  @param requested the CipherSpec that was requested.
     *  @param allowed the CipherSpecs that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedCipherSpecException(CipherSpec requested, CipherSpec[] allowed) {
        this(requested, allowed, null);
    }//UnsupportedCipherSpecException(CipherSpec, CipherSpec[])*/

    /**
     *  Constructor.
     *
     *  @param requested the CipherSpec that was requested.
     *  @param allowed the CipherSpecs that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedCipherSpecException(CipherSpec requested, CipherSpec[] allowed, Throwable cause) {
        super("An unsupported CipherSpec was requested.", 409, requested, allowed, cause);
    }//UnsupportedCipherSpecException(CipherSpec, CipherSpec[], Throwable)*/
}//UnsupportedCipherSpecException*/
