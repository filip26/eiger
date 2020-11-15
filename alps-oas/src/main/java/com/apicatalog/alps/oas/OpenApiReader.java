package com.apicatalog.alps.oas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.apicatalog.alps.Alps;
import com.apicatalog.alps.api.DescriptorBuilder;
import com.apicatalog.alps.api.DocumentBuilder;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.io.DocumentParser;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public final class OpenApiReader implements DocumentParser {

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
        
        // OAS info
        Optional.ofNullable(oas.getInfo()).ifPresent(info -> parseInfo(info, document));

        // OAS servers
        Optional.ofNullable(oas.getServers())
                .orElse(Collections.emptyList())
                .forEach(server -> parseServer(server, document));
        
        // OAS paths
        Optional.ofNullable(oas.getPaths())
                .map(Paths::entrySet)
                .orElse(Collections.emptySet())
                .forEach(e -> parsePath(e.getKey(), e.getValue(), document));

        // OAS components
        Optional.ofNullable(oas.getComponents())
                .map(Components::getSchemas)
                .map(Map::entrySet)
                .orElse(Collections.emptySet())
                .forEach(e -> parseSchema(e.getKey(), e.getValue(), document));

        //TODO
        
        return document.build();
    }

    private static final DescriptorBuilder parseSchema(String key, String name, Schema<?> value) {

        final DescriptorBuilder descriptor = Alps.createDescriptor(DescriptorType.SEMANTIC);
        
        if (value.get$ref() != null) {
            descriptor.href(toHref(value.get$ref()));
                    
        } else {
            descriptor.id(URI.create(key));
        }
        descriptor.name(name);

        
        if ("object".equals(value.getType()) && value.getProperties() != null) {
        
            for (Entry<String, Schema> e : value.getProperties().entrySet()) {
                descriptor.add(parseSchema(key + "-" + e.getKey().toLowerCase(), e.getKey(), e.getValue()));
            }
            
        } else if ("array".equals(value.getType())) {
            
            final Schema<?> items = ((ArraySchema)value).getItems(); 
            
            if (items != null) {
                descriptor.add(parseSchema(key + "-items", null, items));
            }
        }
        
        return descriptor;
    }
    
    private static final void parseSchema(String key, Schema<?> value, DocumentBuilder document) {
        document.add(parseSchema("schema" + "-" + key.toLowerCase(), key, value));
    }
    
    private static final URI toHref(String ref) {
        if (ref.startsWith("#/components/schemas/")) {
            return URI.create("#schema-" + ref.substring("#/components/schemas/".length()).replace("/", "-").toLowerCase());
        }
        return URI.create(ref);
    }

    private static final void parsePath(String path, PathItem item, DocumentBuilder document) {

        for (final Map.Entry<HttpMethod, Operation> op : item.readOperationsMap().entrySet()) {
            
            final DescriptorBuilder descriptor = Alps.createDescriptor(parseMethod(op.getKey()));
            
            Optional.ofNullable(op.getValue().getOperationId())
                    .map(URI::create)
                    .ifPresent(descriptor::id);

            Optional.ofNullable(op.getValue().getSummary())
                    .ifPresent(descriptor::title);

            Optional.ofNullable(op.getValue().getParameters())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(OpenApiReader::parseParameter)
                    .forEach(descriptor::add);
            
            document.add(descriptor);
        }
    }

    private static final DescriptorBuilder parseParameter(final Parameter parameter) {
        
        final DescriptorBuilder descriptor = Alps.createDescriptor(DescriptorType.SEMANTIC);

        descriptor.name(parameter.getName());
        
        return descriptor;
    }
    
    private static final  void parseInfo(final Info info, final DocumentBuilder document) {
        if (info.getTitle() != null && !info.getTitle().isBlank()) {
            document.add(Alps.createDocumentation("text/plain").append(info.getTitle().strip()));
        }
    }
    
    private static final void parseServer(final Server server, final DocumentBuilder document) {

        Optional.ofNullable(server.getUrl())
                .map(URI::create)
                .ifPresent(uri -> document.add(Alps.createLink(uri, "server")));
        
    }
    
    private static final DescriptorType parseMethod(HttpMethod method) {
        
        switch (method.name().toUpperCase()) {
        case "GET":
        case "HEAD":
            return DescriptorType.SAFE;
            
        case "PUT":
        case "OPTIONS":
        case "DELETE":
            return DescriptorType.IDEMPOTENT;
            
        default:
            return DescriptorType.UNSAFE;
        }
    }
}
