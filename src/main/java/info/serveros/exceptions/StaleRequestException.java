package info.serveros.exceptions;

import java.util.Date;
import javax.json.stream.JsonGenerator;
/**
 *  Error for a message malformatted.
 *
 *  @author Francis J.. Van Wetering IV
 */
public class StaleRequestException extends MessageException {

    /**
     *  The timestamp on the request.
     */
    private final Date ts;

    /**
     *  Constructor.
     *
     *  @param ts The timestamp on the request.
     */
    public StaleRequestException(Date ts) {
        this(ts, null);
    }//StaleRequestException(Date)*/

    /**
     *  Constructor.
     *
     *  @param ts The tiemstamp on the request.
     *  @param cause The underlying exception.
     */
    public StaleRequestException(Date ts, Throwable cause) {
        super("Request is Stale.", cause);
        this.ts = (Date) ts.clone();
    }//StaleRequestException(Date, Throwable)*/

    /**
     *  For override by Children to include more info in the json_encodable response body.
     *
     *  @param out The JSON stream to write additional information to.
     */
    @Override
    public void addAdditionalInformation(JsonGenerator out) {
        out.write("timestamp", this.ts.getTime());
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
}//StaleRequestException*/
