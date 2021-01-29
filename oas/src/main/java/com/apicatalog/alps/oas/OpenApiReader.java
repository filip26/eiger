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
import com.apicatalog.alps.DescriptorBuilder;
import com.apicatalog.alps.DocumentBuilder;
import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.dom.DocumentVersion;
import com.apicatalog.alps.dom.element.DescriptorType;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.InvalidDocumentException;
import com.apicatalog.alps.io.DocumentParser;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponses;
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

    private static final Document parseContent(final String content) throws InvalidDocumentException {
        
        final SwaggerParseResult result = new OpenAPIV3Parser().readContents(content);
        //TODO errors?
        
        final OpenAPI oas = result.getOpenAPI();

        if (oas == null) {
            return null;
        }
        
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
                .forEach(e -> parsePath(e.getValue(), document));

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

        final DescriptorBuilder builder = Alps.createDescriptor().type(DescriptorType.SEMANTIC);

        if (value.get$ref() != null) {
            builder.href(toHref(value.get$ref()));
                    
        } else {
            builder.id(URI.create(key));
        }
        builder.name(name);

        
        if ("object".equals(value.getType()) && value.getProperties() != null) {
        
            for (@SuppressWarnings("rawtypes") Entry<String, Schema> e : value.getProperties().entrySet()) {
                builder.add(parseSchema(key + "-" + e.getKey().toLowerCase(), e.getKey(), e.getValue()));
            }
            
        } else if ("array".equals(value.getType())) {
            
            final Schema<?> items = ((ArraySchema)value).getItems(); 
            
            if (items != null) {
                builder.add(parseSchema(key + "-items", null, items));
            }
        }
        
        return builder;
    }
    
    private static final void parseSchema(String key, Schema<?> value, DocumentBuilder document) {
        document.add(parseSchema("model" + "-" + key.toLowerCase(), key, value));
    }
    
    private static final URI toHref(String ref) {
        if (ref.startsWith("#/components/schemas/")) {
            return URI.create("#model-" + ref.substring("#/components/schemas/".length()).replace("/", "-").toLowerCase());
        }
        return URI.create(ref);
    }

    private static final void parsePath(PathItem item, DocumentBuilder document) {

        for (final Map.Entry<HttpMethod, Operation> op : item.readOperationsMap().entrySet()) {
            
            final DescriptorBuilder builder = Alps.createDescriptor().type(parseMethod(op.getKey()));
            
            Optional.ofNullable(op.getValue().getOperationId())
                    .map(URI::create)
                    .ifPresent(builder::id);

            Optional.ofNullable(op.getValue().getSummary())
                    .ifPresent(builder::title);

            Optional.ofNullable(op.getValue().getParameters())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(OpenApiReader::parseParameter)
                    .forEach(builder::add);
            
            Optional.ofNullable(op.getValue().getResponses())
                    .map(OpenApiReader::parseResponses)
                    .ifPresent(builder::returnType);
            
            document.add(builder);
        }
    }

    private static final URI parseResponses(final ApiResponses responses) {
        
        if (responses.containsKey("200")) {
            
            for (Map.Entry<String, MediaType> mediaType : responses.get("200").getContent().entrySet()) {

                if (mediaType.getValue().getSchema() == null) {
                    continue;
                }
                
                if (mediaType.getValue().getSchema().get$ref() != null) {
             
                    try {
                        return toHref(mediaType.getValue().getSchema().get$ref());
                        
                    } catch (IllegalArgumentException e) {
                        //TODO ignored, print warning
                    }
                    
                } else if (mediaType.getValue().getSchema().getType() != null) {
                    
                    //TODO
                    
                } else if (mediaType.getValue().getExamples() != null) {
                    
                    //TODO
                    
                }
            }
        }

        return null;
    }
    
    private static final DescriptorBuilder parseParameter(final Parameter parameter) {
        
        final DescriptorBuilder descriptor = Alps.createDescriptor().type(DescriptorType.SEMANTIC);

        descriptor.name(parameter.getName());
        
        return descriptor;
    }
    
    private static final void parseInfo(final Info info, final DocumentBuilder builder) {
        if (info.getTitle() != null && !info.getTitle().isBlank()) {
            builder.add(Alps.createDocumentation().type("text/plain").append(info.getTitle().strip()));
        }

        if (info.getDescription() != null && !info.getDescription().isBlank()) {
            builder.add(Alps.createDocumentation().type("text/plain").append(info.getDescription()));
        }
    }
    
    private static final void parseServer(final Server server, final DocumentBuilder document) {

        try {
        
            Optional.ofNullable(server.getUrl())
                    .map(URI::create)
                    .ifPresent(uri -> document.add(Alps.createLink(uri, "server")));
                
        } catch (IllegalArgumentException e) {
            //TODO ignored, print warning
            
        }
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
