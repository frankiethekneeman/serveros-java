package info.serveros.exceptions;

/**
 *  Error for a message malformatted.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class NonceMismatchException extends MessageException {

    /**
     *  Constructor.
     *
     *  @param ts The timestamp on the request.
     */
    public NonceMismatchException(String nonce) {
        this(nonce, null);
    }//NonceMismatchException(Date)*/

    /**
     *  Constructor.
     *
     *  @param ts The tiemstamp on the request.
     *  @param cause The underlying exception.
     */
    public NonceMismatchException(String nonce, Throwable cause) {
        super(nonce + " nonce does not match.", cause);
    }//NonceMismatchException(Date, Throwable)*/
}//NonceMismatchException*/
