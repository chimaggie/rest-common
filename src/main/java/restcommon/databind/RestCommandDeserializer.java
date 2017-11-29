package restcommon.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by maggie on 4/19/16.
 */
public class RestCommandDeserializer extends JsonDeserializer<RestCommand> {
    @Override
    public RestCommand deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}
