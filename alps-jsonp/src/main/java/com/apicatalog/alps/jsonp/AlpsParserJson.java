package com.apicatalog.alps.jsonp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.apicatalog.alps.AlpsParser;
import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

public final class AlpsParserJson implements AlpsParser {

    @Override
    public boolean canParse(final String mediaType) {
        return mediaType != null
                && ("application/json".equalsIgnoreCase(mediaType)
                    || mediaType.endsWith("+json")
                    );
    }

    @Override
    public AlpsDocument parse(final URI baseUri, final String mediaType, final InputStream stream) throws IOException, AlpsParserException {

        if (stream == null) {
            throw new IllegalArgumentException();
        }
        
        if (!canParse(mediaType)) {
            throw new AlpsParserException();
        }
        
        return parse(baseUri, Json.createParser(stream));
    }

    @Override
    public AlpsDocument parse(final URI baseUri, final String mediaType, final Reader reader) throws IOException, AlpsParserException {

        if (reader == null) {
            throw new IllegalArgumentException();
        }
        
        if (!canParse(mediaType)) {
            throw new AlpsParserException();
        }
        
        return parse(baseUri, Json.createParser(reader));
    }
    
    private static final AlpsDocument parse(URI baseUri, JsonParser parser)  throws AlpsParserException {
        
        if (!parser.hasNext()) {
            throw new AlpsParserException("Expected JSON object but was an empty input");
        }
            
        final Event event = parser.next();
        
        if (!Event.START_OBJECT.equals(event)) {
            throw new AlpsParserException("Expected JSON object but was " + event);
        }
        
        final JsonObject rootObject = parser.getObject();
        
        if (!rootObject.containsKey(JsonKeys.ROOT)) {
            throw new AlpsParserException("Property '" + JsonKeys.ROOT + "' is not present");
        }
        
        final JsonValue alpsObject = rootObject.get(JsonKeys.ROOT);
        
        if (JsonUtils.isNotObject(alpsObject)) {
            throw new AlpsParserException("Property '" + JsonKeys.ROOT + "' does not contain JSON object");
        }
            
        return JsonDocument.parse(baseUri, alpsObject.asJsonObject());
    }
}
