package com.apicatalog.alps.jsonp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Set;

import javax.json.Json;
import javax.json.stream.JsonParser;

import com.apicatalog.alps.AlpsParser;
import com.apicatalog.alps.AlpsParserException;
import com.apicatalog.alps.dom.AlpsDocument;

public final class AlpsJsonParser implements AlpsParser {

    @Override
    public Set<String> mediaTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlpsDocument parse(URI baseUri, String mediaType, InputStream stream) throws IOException, AlpsParserException {
        
        //TODO check mediaType
        
        return parse(baseUri, Json.createParser(stream));
    }

    @Override
    public AlpsDocument parse(URI baseUri, String mediaType, Reader reader) throws IOException, AlpsParserException {
        //TODO check mediaType
        
        return parse(baseUri, Json.createParser(reader));
    }
    
    private AlpsDocument parse(URI baseUri, JsonParser parser)  throws IOException, AlpsParserException {
        //TODO
        return null;
    }

}
