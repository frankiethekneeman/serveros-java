package info.serveros.exceptions;

import info.serveros.algorithms.HashSpec;
import javax.json.stream.JsonGenerator;

/**
 *  Thrown when an unsupported HashSpec is encountered.
 */
public class UnsupportedHashSpecException extends UnsupportedException {

    /**
     *  Constructor.
     *
     *  @param requested the HashSpec that was requested.
     *  @param allowed the HashSpecs that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedHashSpecException(HashSpec requested, HashSpec[] allowed) {
        this(requested, allowed, null);
    }//UnsupportedHashSpecException(HashSpec, HashSpec[])*/

    /**
     *  Constructor.
     *
     *  @param requested the HashSpec that was requested.
     *  @param allowed the HashSpecs that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedHashSpecException(HashSpec requested, HashSpec[] allowed, Throwable cause) {
        super("An unsupported HashSpec was requested.", 490, requested, allowed, cause);
    }//UnsupportedHashSpecException(HashSpec, HashSpec[], Throwable)*/
}//UnsupportedHashSpecException*/
