package com.apicatalog.alps.oas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.stream.Collectors;

import com.apicatalog.alps.DocumentParser;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public final class OpenAPI2ALPSAdapter implements DocumentParser {

    @Override
    public Document parse(URI baseUri, InputStream stream) throws IOException, DocumentParserException {
        
        final String content = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
        
        return parseContent(content);
    }

    @Override
    public Document parse(URI baseUri, Reader reader) throws IOException, DocumentParserException {
        final String content = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));

        return parseContent(content);
    }

    private Document parseContent(final String content) {
        
        final SwaggerParseResult result = new OpenAPIV3Parser().readContents(content);

        //TODO errors?

        //TODO
        return null;
    }
    
}
