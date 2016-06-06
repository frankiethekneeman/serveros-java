package info.serveros.exceptions;

import javax.json.stream.JsonGenerator;
/**
 *  Error for a message malformatted.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class UnrecognizedResponseCodeException extends MessageException {

    /**
     *  The unrecognized code.
     */
    public final int code;

    /**
     *  Constructor.
     *
     *  @param code the unrecognized code.
     */
    public UnrecognizedResponseCodeException(int code) {
        this(code, null);
    }//UnrecognizedResponseCodeException(Date)*/

    /**
     *  Constructor.
     *
     *  @param code the unrecognized code.
     *  @param cause The underlying exception.
     */
    public UnrecognizedResponseCodeException(int code, Throwable cause) {
        super("An unrecognized Response Code was encountered.", cause);
        this.code = code;
    }//UnrecognizedResponseCodeException(Date, Throwable)*/

    /**
     *  For override by Children to include more info in the json_encodable response body.
     *
     *  @param out The JSON stream to write additional information to.
     */
    @Override
    public void addAdditionalInformation(JsonGenerator out) {
        out.write("code", this.code);
    }//addAdditionalInformaiton(JsonGenerator)*/

    /**
     *  Set to true if additional information exists.
     *
     *  @return a boolean - true it there exists additional information.  False otherwise.
     */
    @Override
    public boolean hasAdditionalInformation() {
        return true;
    }//hasAdditionalInformation()*/
}//UnrecognizedResponseCodeException*/
