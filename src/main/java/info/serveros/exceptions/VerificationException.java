package info.serveros.exceptions;

/**
 *  Error for an unverified Message.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class VerificationException extends MessageException {

    /**
     *  Constructor
     */
    public VerificationException() {
        this(null);
    }//VerificationException()*/

    /**
     *  Constructor.
     *
     *  @param cause The underlying exception.
     */
    public VerificationException(Throwable cause) {
        super("Verifier Returned False.", cause);
    }//VerificationException(Throwable)*/
}//VerificationException()*/
