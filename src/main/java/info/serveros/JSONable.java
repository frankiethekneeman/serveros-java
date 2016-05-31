package info.serveros;

import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import java.io.StringReader;
import javax.json.JsonValue;
import java.io.Writer;
import java.io.OutputStream;

/**
 *  A thing that can be Output as JSON.
 */
public abstract class JSONable implements JsonValue{

    /**
     *  The JSON encoded string.
     *
     *  @return the JSON encoded string.
     */
    @Override
    public String toString() {
        return this.toJSON();
    }//toString()*/

    /**
     *  Turn this object into JSON.
     *
     *  @param g The JSON Generator to output to.
     */
    public void toJSON(JsonGenerator g) {
        this.toJSON(g, null);
    }//toJSON(JsonGenerator)*/

    /**
     *  Turn this object into JSON.
     *
     *  @param g The JSON Generator to output to.
     *  @param name The name to use for the object - or null to just output an object.
     */
    public void toJSON(JsonGenerator g, String name) {
        if (name == null)
            g.writeStartObject();
        else
            g.writeStartObject(name);
        this.jsonHelper(g);

        g .writeEnd();
    }//toJSON(JsonGenerator, String)*/

    /**
     *  Turn this object into JSON.
     *
     *  @return a string representing this object in JSON form.
     */
    public String toJSON() {
        StringWriter w = new StringWriter();
        this.toJSON(w);
        w.flush();
        return w.toString();
    }//toJSON()*/

    /**
     *  An internal to handle the JsonGenerator.  Flushes and closes the generator.
     *
     *  @param g The JsonGenerator to write to.
     */
    private void handleGenerator(JsonGenerator g) {
        this.toJSON(g);
        g.flush();
        g.close();
    }//handleGenerator(JsonGenerator)*/

    /**
     *  Write the JSON to a Writer.
     *
     *  @param w The writer to write to.
     */
    public void toJSON(Writer w) {
        this.handleGenerator(Json.createGenerator(w));
    }//toJSON(Writer)*/

    /**
     *  Write JSON to an OutputStream
     *
     *  @param o OutputStream to write to.
     */
    public void toJSON(OutputStream o) {
        this.handleGenerator(Json.createGenerator(o));
    }//toJson(OutputStream)*/

    /**
     *  Add Json elements of the current object to the already started JSON object.
     *
     *  @param g The JSON Generator to output to.
     */
    protected abstract void jsonHelper(JsonGenerator g) ;

    /**
     *  Get the type of value this is - an Object.
     *
     *  @return JsonValue.ValueType.OBJECT
     */
    @Override
    public JsonValue.ValueType getValueType() {
        return JsonValue.ValueType.OBJECT;
    }//getValueType()*/

    /**
     *  Unwrap the Json to a JsonObject that can be referenced.
     *
     *  @param json A json object encoded as a string.
     *
     *  @return the decoded object.
     */
    public static JsonObject unJSON(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject obj = reader.readObject();
        reader.close();
        return obj;
    }//unJSON(String)*/
}//JSONable*/
