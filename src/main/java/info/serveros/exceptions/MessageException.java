package info.serveros.exceptions;

/**
 *  Error for a message malformatted.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class MessageException extends ServerosException {

    /**
     *  Constructor
     *
     *  @param message The message about the exception.
     *  @param cause The underlying Exception.
     */
    public MessageException(String message, Throwable cause) {
        super(message, 422, cause);
    }//MessageException(String, Throwable)*/

    /**
     *  Constructor.
     *
     *  @param message The message about the exception.
     */
    public MessageException(String message) {
        this(message, null);
    }//MessageException(String)*/
}//MessageException*/
