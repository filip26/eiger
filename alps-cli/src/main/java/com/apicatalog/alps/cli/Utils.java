package com.apicatalog.alps.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.apicatalog.alps.DocumentParser;
import com.apicatalog.alps.jsonp.JsonDocumentParser;
import com.apicatalog.alps.xml.XmlDocumentParser;

final class Utils {

    private Utils() {
    }
    
    static final String getMediaType(final String type, final String path, final boolean input) {
        
        if ("xml".equalsIgnoreCase(type)) {
            return "application/xml";
        }

        if ("json".equalsIgnoreCase(type)) {
            return "application/json";
        }

        if (type != null) {
            throw new IllegalArgumentException("Unknown file type [" + type + "], expected [xml] or [json].");
        }
        
        if (path != null && (path.toLowerCase().endsWith(".xml") || path.toLowerCase().endsWith("+xml"))) {
            return "application/xml";
        }

        if (path != null && (path.toLowerCase().endsWith(".json") || path.toLowerCase().endsWith("+json"))) {
            return "application/json";
        }
        
        if (path != null) {
            throw new IllegalArgumentException("Can not determine " + (input ? "input" : "output") + " file type [" + path + "], please add --" + (input ? "source" : "target")  + "=(json|xml) argument.");
        }

        throw new IllegalArgumentException("Can not determine " + (input ? "input" : "output") + " type, please add --" + (input ? "source" : "target")  + "=(json|xml) argument.");
    }

    static final InputStream fileToInputStream(final String path) {
        
        final File file = new File(path);
        
        if (!file.exists()) {
            System.err.println("File '" + path + "' does not exist.");            
            return null;
        }

        if (!file.canRead()) {
            System.err.println("Input file '" + path + "' is not readable.");
            return null;
        }
        
        try {
            return new FileInputStream(file);
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return null;
    }
  
    static final DocumentParser getParser(final String mediaType) {
        
        if ("application/json".equals(mediaType)) {
            return new JsonDocumentParser();
        }

        if ("application/xml".equals(mediaType)) {
            return new XmlDocumentParser();
        }

        throw new IllegalArgumentException("Unsupported media type [" + mediaType + "].");
    }    
}
