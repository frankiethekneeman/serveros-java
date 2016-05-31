package info.serveros.exceptions;

import javax.json.stream.JsonGenerator;

/**
 *  The base error for all Errors.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class ServerosException extends Exception {

    /**
     *  The default Messsage.
     */
    public static final String DEFAULT_MESSAGE = "No Message Provided.";

    /**
     *  The default status code.
     */
    public static final int DEFAULT_STATUS = 500;

    /**
     *  The status Code for HTTP Responses
     */
    public final int statusCode;

    /**
     *  Constructor
     *
     *  @param message A message about the kind of error that happened.
     *  @param statusCode A status code for use.
     *  @param cause the Exception that caused this exception.
     */
    public ServerosException(String message, int statusCode, Throwable cause) {
        super(("".equals(message) || message == null) ? ServerosException.DEFAULT_MESSAGE : message, cause);
        this.statusCode = statusCode;
    }//ServerosException(String, int, Throwable)*/

    /**
     *  Constructor
     *
     *  @param message A message about the kind of error that happened.
     *  @param statusCode A status code for use.
     */
    public ServerosException(String message, int statusCode) {
        this(message, statusCode, null);
    }//ServerosException(String, int)*/

    /**
     *  Constructor
     *
     *  @param message A message about the kind of error that happened.
     *  @param cause the Exception that caused this exception.
     */
    public ServerosException(String message, Throwable cause) {
        this(message, ServerosException.DEFAULT_STATUS, cause);
    }//ServerosException(String, Throwable)*/

    /**
     *  Constructor
     *
     *  @param statusCode A status code for use.
     *  @param cause the Exception that caused this exception.
     */
    public ServerosException(int statusCode, Throwable cause) {
        this(null, statusCode, cause);
    }//ServerosException(int, Throwable)*/

    /**
     *  Constructor
     *
     *  @param message A message about the kind of error that happened.
     */
    public ServerosException(String message) {
        this(message, null);
    }//ServerosException(String)*/

    /**
     *  Constructor
     *
     *  @param statusCode A status code for use.
     */
    public ServerosException(int statusCode) {
        this(statusCode, null);
    }//ServerosException(int)*/

    /**
     *  Constructor
     *
     *  @param cause the Exception that caused this exception.
     */
    public ServerosException(Throwable cause) {
        this(ServerosException.DEFAULT_STATUS, cause);
    }//ServerosExceptionA(Throwable)*/

    /**
     *  Constructor.  Defaults status.
     */
    public ServerosException() {
        this(ServerosException.DEFAULT_STATUS);
    }//ServerosException()*/

    /**
     *  Prep a Json_encodable response body for the error.
     *
     *  @return array An Array with information about the error.
     */
    public void toJson(JsonGenerator out, String name) {
        if ( name == null || "".equals(name)) {
            out.writeStartObject();
        } else {
            out.writeStartObject(name);
        }
        out.write("message", this.getMessage());
        out.write("status", this.statusCode);
        if (this.hasAdditionalInformation()) {
            out.writeStartObject("additionalInformation");
            this.addAdditionalInformation(out);
            out.writeEnd();
        }
        out.writeEnd();
    }//toJson(JsonGenerator, String)*/

    /**
     *  For override by Children to include more info in the json_encodable response body.
     *
     *  @param out The JSON stream to write additional information to.
     */
    public void addAdditionalInformation(JsonGenerator out) {
    }//addAdditionalInformaiton(JsonGenerator)*/

    /**
     *  Set to true if additional information exists.
     *
     *  @return a boolean - true it there exists additional information.  False otherwise.
     */
    public boolean hasAdditionalInformation() {
        return false;
    }//hasAdditionalInformation()*/

}//ServerosException*/
