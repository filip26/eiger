package com.apicatalog.alps.jsonp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.json.Json;
import javax.json.JsonWriter;

import com.apicatalog.alps.AlpsWriter;
import com.apicatalog.alps.AlpsWriterException;
import com.apicatalog.alps.dom.AlpsDocument;

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

    private void write(AlpsDocument document, JsonWriter jsonWriter) throws IOException, AlpsWriterException {
        jsonWriter.write(JsonDocument.toJson(document));
    }
}
