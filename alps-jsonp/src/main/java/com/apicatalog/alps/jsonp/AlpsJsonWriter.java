package com.apicatalog.alps.jsonp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import com.apicatalog.alps.AlpsWriter;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;
import com.apicatalog.alps.dom.AlpsVersion;

public final class AlpsJsonWriter implements AlpsWriter {

    @Override
    public boolean canWrite(String mediaType) {
        return mediaType != null
                && ("application/json".equalsIgnoreCase(mediaType)
                    || mediaType.toLowerCase().endsWith("+json")
                    );
    }
    
    @Override
    public void write(String mediaType, AlpsDocument document, OutputStream stream) throws IOException, AlpsWriterException {
        
        //TODO check media type and arguments
        
        write(document, Json.createWriter(stream));
    }

    @Override
    public void write(String mediaType, AlpsDocument document, Writer writer) throws IOException, AlpsWriterException {

        //TODO check media type and arguments

        write(document, Json.createWriter(writer));
    }

    public static final JsonObject toJsonObject(AlpsDocument document) throws AlpsWriterException {
        
        final JsonObjectBuilder alps = Json.createObjectBuilder();
        
        // version
        writeVersion(document.getVersion(), alps);
        
        return Json.createObjectBuilder().add(AlpsJsonKeys.ROOT, alps).build();
    }

    private static void writeVersion(AlpsVersion version, JsonObjectBuilder alps) {
        alps.add(AlpsJsonKeys.VERSION, "1.0");    
    }
    
    private void write(AlpsDocument document, JsonWriter jsonWriter) throws IOException, AlpsWriterException {
        jsonWriter.write(toJsonObject(document));
    }
}
