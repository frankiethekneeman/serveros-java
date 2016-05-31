package info.serveros.exceptions;

import javax.json.stream.JsonGenerator;
/**
 *  Error for a message malformatted.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class UnrecognizedResponseCodeException extends MessageException {

    /**
     *  The timestamp on the request.
     */
    public final short code;

    /**
     *  Constructor.
     *
     *  @param ts The timestamp on the request.
     */
    public UnrecognizedResponseCodeException(short code) {
        this(code, null);
    }//UnrecognizedResponseCodeException(Date)*/

    /**
     *  Constructor.
     *
     *  @param ts The tiemstamp on the request.
     *  @param cause The underlying exception.
     */
    public UnrecognizedResponseCodeException(short code, Throwable cause) {
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
