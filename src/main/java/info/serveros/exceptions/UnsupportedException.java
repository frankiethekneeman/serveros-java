package info.serveros.exceptions;

import javax.json.stream.JsonGenerator;

/**
 *  Thrown when an unsupported Object is encountered.
 */
public abstract class UnsupportedException extends ServerosException {

    /**
     *  The Object that was reqeusted.
     */
    public final Object requested;

    /**
     *  The algorithms that are supported.
     */
    private final Object[] allowed;

    /**
     *  Constructor.
     *
     *  @param requested the Object that was requested.
     *  @param allowed the Objects that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedException(String message, int statusCode, Object requested, Object[] allowed) {
        this(message, statusCode, requested, allowed, null);
    }//UnsupportederException(Object, Object[])*/

    /**
     *  Constructor.
     *
     *  @param requested the Object that was requested.
     *  @param allowed the Objects that are allowed.
     *  @param cause The exception that caused this exception to be thrown.
     */
    public UnsupportedException(String message, int statusCode, Object requested, Object[] allowed, Throwable cause) {
        super(message, statusCode, cause);
        this.requested = requested;
        this.allowed = allowed.clone();
    }//UnsupportedException(Object, Object[], Throwable)*/

    /**
     *  Get the allowed algorithms list.
     *
     *  @return a clone of the internal allowed Objects list.
     */
    public Object[] getAllowed() {
        return this.allowed.clone();
    }//getAllowed()*/

    /**
     *  For override by Children to include more info in the json_encodable response body.
     *
     *  @param out The JSON stream to write additional information to.
     */
    @Override
    public void addAdditionalInformation(JsonGenerator out) {
        out.write("requested", this.requested.toString());
        out.writeStartArray("allowed");
        for(Object a: this.allowed)
            out.write(a.toString());
        out.writeEnd();
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
}//UnsupportedCipherException*/
