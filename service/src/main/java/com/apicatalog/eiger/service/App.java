package com.apicatalog.eiger.service;

import static io.vertx.ext.web.validation.builder.Parameters.optionalParam;
import static io.vertx.json.schema.common.dsl.Schemas.*;
import static com.apicatalog.eiger.service.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.io.DocumentParser;
import com.apicatalog.alps.io.DocumentWriter;
import com.apicatalog.alps.json.JsonDocumentParser;
import com.apicatalog.alps.json.JsonDocumentWriter;
import com.apicatalog.alps.oas.OpenApiReader;
import com.apicatalog.alps.xml.XmlDocumentParser;
import com.apicatalog.alps.xml.XmlDocumentWriter;
import com.apicatalog.alps.yaml.YamlDocumentWriter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.RequestPredicate;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;

public class App extends AbstractVerticle {

    Instant startTime;

    @Override
    public void start() throws Exception {

        final SchemaRouter schemaRouter = SchemaRouter.create(vertx, new SchemaRouterOptions());
        final SchemaParser schemaParser = SchemaParser.createDraft201909SchemaParser(schemaRouter);

        final Router router = Router.router(vertx);

        router.post().handler(BodyHandler.create().setBodyLimit(250000));

        // validate parameters
        router.post(PATH_TRANSFORM)
                    // transformer options validation
                    .handler(
                        ValidationHandlerBuilder
                                .create(schemaParser)
                                .queryParameter(optionalParam(PARAM_PRETTY, booleanSchema()))
                                .queryParameter(optionalParam(PARAM_VERBOSE, booleanSchema()))
                                .queryParameter(optionalParam(PARAM_BASE, stringSchema()))
                                .predicate(RequestPredicate.BODY_REQUIRED)      // request body is required
                                .build()
                        )
                    // transformer options extraction
                    .handler(ctx -> {
                        final RequestParameters parameters = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);

                        final RequestParameter pretty = parameters.queryParameter(PARAM_PRETTY);
                        ctx.put(PARAM_PRETTY, pretty != null && pretty.getBoolean());

                        final RequestParameter verbose = parameters.queryParameter(PARAM_VERBOSE);
                        ctx.put(PARAM_VERBOSE, verbose != null && verbose.getBoolean());

                        final RequestParameter base = parameters.queryParameter(PARAM_BASE);
                        
                        try {
                            
                            ctx.put(PARAM_BASE, base != null && !base.getString().isBlank() ? URI.create(base.getString().strip()) : null);
                            ctx.next();
                            
                        } catch (IllegalArgumentException e) {
                            ctx.response().setStatusCode(400).putHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_TEXT_PLAIN).end("Base [" + base.getString() + "] is not valid URI." );
                        }

                    });

        // xml -> alps
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new XmlDocumentParser()));

        // json -> alps
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new JsonDocumentParser()));

        // oas -> alps
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_OPEN_API)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new OpenApiReader()));

        // alps -> xml | json | yaml
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_ALPS_XML)
                .consumes(MEDIA_TYPE_ALPS_JSON)
                .consumes(MEDIA_TYPE_OPEN_API)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(ctx -> {

                    final HttpServerResponse response = ctx.response();

                    String acceptableContentType = ctx.getAcceptableContentType();

                    final StringWriter target = new StringWriter();

                    try (final DocumentWriter writer = getWriter(
                                                            acceptableContentType,
                                                            ctx.get(PARAM_PRETTY),
                                                            ctx.get(PARAM_VERBOSE),
                                                            target
                                                            )) {

                        writer.write(ctx.get(SOURCE));

                        response.setStatusCode(200);
                        response.putHeader(HEADER_CONTENT_TYPE, acceptableContentType);
                        response.end(target.toString());

                    } catch (DocumentWriterException e) {
                        response.setStatusCode(500);
                        response.putHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_TEXT_PLAIN);
                        response.end(e.getMessage());

                    } catch (Throwable e) {
                        response.setStatusCode(500);
                        response.putHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_TEXT_PLAIN);
                        response.end(e.getMessage());
                    }
                });

        // static resources
        router.get().handler(StaticHandler
                                    .create()
                                    .setFilesReadOnly(true)
                                    .setDirectoryListing(false)
                            );

        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(getDefaultPort())
                .onSuccess(ctx -> {
                    System.out.println("Eiger HTTP node started on port " + ctx.actualPort() + ".");
                    startTime = Instant.now();
                })
                .onFailure(ctx ->
                    System.err.println("Eiger HTTP node start failed [" + ctx.getMessage() + "].")
                );
    }

    @Override
    public void stop() throws Exception {
        if (startTime != null) {
            System.out.println("Eiger HTTP node stopped after running for " +  DurationFormatUtils.formatDurationWords(Duration.between(startTime, Instant.now()).toMillis(), true, true) + ".");
        }
        super.stop();
    }

    static final int getDefaultPort() {
        final String envPort = System.getenv("PORT");

        if (envPort != null) {
            return Integer.valueOf(envPort);
        }
        return 8080;
    }

    static final DocumentWriter getWriter(final String target, final boolean pretty, final boolean verbose, final Writer writer) throws DocumentWriterException {

        if (MEDIA_TYPE_ALPS_JSON.equals(target)) {
            return JsonDocumentWriter.create(writer, pretty, verbose);

        } else if (MEDIA_TYPE_ALPS_XML.equals(target)) {
            return XmlDocumentWriter.create(writer, pretty, verbose);

        } else if (MEDIA_TYPE_ALPS_YAML.equals(target)) {
            return YamlDocumentWriter.create(writer, verbose);
        }

        throw new IllegalStateException();
    }

    static class ReaderHandler implements Handler<RoutingContext> {

        final DocumentParser parser;

        public ReaderHandler(DocumentParser parser) {
            this.parser = parser;
        }

        @Override
        public void handle(RoutingContext ctx) {
            try {

                final Document document = parser.parse(ctx.get(PARAM_BASE), new ByteArrayInputStream(ctx.getBody().getBytes()));

                if (document == null) {
                    ctx.end();
                    return;
                }

                ctx.put(SOURCE, document).next();

            } catch (DocumentParserException e) {

                final HttpServerResponse response = ctx.response();
                response.setStatusCode(400);
                response.putHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_TEXT_PLAIN);
                response.end(e.getMessage());

            } catch (Throwable e) {

                final HttpServerResponse response = ctx.response();
                response.setStatusCode(500);
                response.putHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_TEXT_PLAIN);
                response.end(e.getMessage());
            }
        }
    }
}
