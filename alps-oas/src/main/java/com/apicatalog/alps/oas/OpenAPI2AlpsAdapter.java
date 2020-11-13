package com.apicatalog.alps.oas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.api.DocumentBuilder;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.io.DocumentParser;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public final class OpenAPI2AlpsAdapter implements DocumentParser {

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
        
        final OpenAPI oas = result.getOpenAPI();

        final DocumentBuilder document = Alps.createDocument(DocumentVersion.VERSION_1_0);
        
        // parse info
        Optional.ofNullable(oas.getInfo()).ifPresent(info -> parseInfo(info, document));

        // parse servers
        Optional.ofNullable(oas.getServers())
                .orElse(Collections.emptyList())
                .forEach(server -> parseServer(server, document));
        
        //TODO
        
        return document.build();
    }

    private static final  void parseInfo(final Info info, final DocumentBuilder document) {
        if (info.getTitle() != null && !info.getTitle().isBlank()) {
            document.add(Alps.createDocumentation("text/plain").append(info.getTitle().strip()));
        }
    }
    
    private static final void parseServer(final Server server, final DocumentBuilder document) {
        
        Optional.ofNullable(server.getUrl()).ifPresent(url -> document.add(Alps.createLink(URI.create(url), "server")));
        
    }
}
