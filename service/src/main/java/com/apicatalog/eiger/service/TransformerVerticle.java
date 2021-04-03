package com.apicatalog.eiger.service;

import static com.apicatalog.eiger.service.Constants.*;
import static io.vertx.ext.web.validation.builder.Parameters.*;
import static io.vertx.json.schema.common.dsl.Schemas.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.apicatalog.alps.dom.Document;
import com.apicatalog.alps.error.DocumentParserException;
import com.apicatalog.alps.error.DocumentWriterException;
import com.apicatalog.alps.error.MalformedDocumentException;
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
import io.vertx.core.json.JsonObject;
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

public class TransformerVerticle extends AbstractVerticle {

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
                        ctx.response().setStatusCode(400).putHeader(HEADER_CONTENT_TYPE, contentTypeValue(MEDIA_TYPE_TEXT_PLAIN)).end("Base [" + (base != null ? base.getString() : "null") + "] is not valid URI." );
                    }
                });

        // XML -> XML | JSON | YAML
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new XmlDocumentParser()))
                .handler(new WriterHandler())
                .failureHandler(new ErrorHandler());

        // JSON -> XML | JSON | YAML
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new JsonDocumentParser()))
                .handler(new WriterHandler())
                .failureHandler(new ErrorHandler());

        // OpenAPI -> XML | JSON | YAML
        router.post(PATH_TRANSFORM)
                .consumes(MEDIA_TYPE_OPEN_API)
                .produces(MEDIA_TYPE_ALPS_XML)
                .produces(MEDIA_TYPE_ALPS_JSON)
                .produces(MEDIA_TYPE_ALPS_YAML)
                .handler(new ReaderHandler(new OpenApiReader()))
                .handler(new WriterHandler())
                .failureHandler(new ErrorHandler());

        // static resources
        router.get().handler(StaticHandler
                                    .create()
                                    .setIncludeHidden(false)
                                    .setDefaultContentEncoding("UTF-8")
                                    .setMaxAgeSeconds(600l)     // maxAge = 10 min
                            );

        // server
        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(getDefaultPort())
                .onSuccess(ctx -> {
                    System.out.println("Transformer verticle started on port " + ctx.actualPort() + " with " + Charset.defaultCharset()  + " charset.");
                    startTime = Instant.now();
                })
                .onFailure(ctx ->
                    System.err.println("Transformer verticle start failed [" + ctx.getMessage() + "].")
                );
    }

    @Override
    public void stop() throws Exception {
        if (startTime != null) {
            System.out.println("Transformer verticle stopped after running for " +  DurationFormatUtils.formatDurationWords(Duration.between(startTime, Instant.now()).toMillis(), true, true) + ".");
        }
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
                    ctx.response().end();
                    return;
                }

                ctx.put(SOURCE, document).next();

            } catch (Exception e) {
                ctx.fail(e);
            }
        }
    }

    static class WriterHandler implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext ctx) {

            final String acceptableContentType = ctx.getAcceptableContentType();

            final StringWriter target = new StringWriter();

            try (final DocumentWriter writer = getWriter(
                                                    acceptableContentType,
                                                    ctx.get(PARAM_PRETTY),
                                                    ctx.get(PARAM_VERBOSE),
                                                    target
                                                    )) {

                writer.write(ctx.get(SOURCE));

                ctx.response()
                        .setStatusCode(200)
                        .putHeader(HEADER_CONTENT_TYPE, contentTypeValue(acceptableContentType))
                        .end(target.toString());

            } catch (Exception e) {
                ctx.fail(e);
            }
        }
    }

    static class ErrorHandler implements Handler<RoutingContext> {

        @Override
        public void handle(RoutingContext ctx) {

            final Throwable e = ctx.failure();

            if (e instanceof DocumentParserException) {
                returnFormattedError(ctx, e);
                return;
            }

            ctx.response()
                    .setStatusCode(500)
                    .putHeader(HEADER_CONTENT_TYPE, contentTypeValue(MEDIA_TYPE_TEXT_PLAIN))
                    .end(e.getMessage());
        }
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

    static final int getDefaultPort() {
        final String envPort = System.getenv("PORT");

        if (envPort != null) {
            return Integer.valueOf(envPort);
        }
        return 8080;
    }

    static final String contentTypeValue(final String mediaType) {
        return mediaType + "; charset=" + Charset.defaultCharset();
    }

    static final void returnFormattedError(final RoutingContext ctx, Throwable e) { 

        final JsonObject error = new JsonObject();
        
        error.put("message", e.getMessage());
        
        if (e instanceof MalformedDocumentException) {
    
            final MalformedDocumentException me = (MalformedDocumentException)e;
  
            error.put("location", new JsonObject().put("line", me.getLineNumber()).put("column", me.getColumnNumber()));
        }
        
        if (ctx.get(Constants.PARAM_BASE) != null) {
            error.put("base", ctx.get(Constants.PARAM_BASE));
        }
        
        error.put("mediaType", ctx.parsedHeaders().contentType().component() + "/" + ctx.parsedHeaders().contentType().subComponent());
        
        ctx.response()
            .setStatusCode(400)
            .putHeader(HEADER_CONTENT_TYPE, contentTypeValue(MEDIA_TYPE_JSON));

        if ((boolean)ctx.get(Constants.PARAM_PRETTY)) {
            ctx.end(error.encodePrettily());
            
        } else {
            ctx.json(error);
        }
    }
}