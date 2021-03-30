package com.apicatalog.eiger.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class App extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        final Router router = Router.router(vertx);

        router.route("/").method(HttpMethod.GET).handler(ctx -> ctx.reroute("/index.html"));

        router.post().handler(BodyHandler.create().setBodyLimit(250000));

        router.route()
              .method(HttpMethod.POST)
              .path("/transform")
              .consumes(Constants.MEDIA_TYPE_ALPS_XML)
              .consumes(Constants.MEDIA_TYPE_ALPS_JSON)
              .consumes(Constants.MEDIA_TYPE_OPEN_API)
              .produces(Constants.MEDIA_TYPE_ALPS_XML)
              .produces(Constants.MEDIA_TYPE_ALPS_JSON)
              .produces(Constants.MEDIA_TYPE_ALPS_YAML)

              .handler(ctx -> {

                      Buffer body = ctx.getBody();

                      HttpServerResponse response = ctx.response();

                      String acceptableContentType = ctx.getAcceptableContentType();

                      boolean verbose = ctx.queryParam("verbose").stream().findFirst().map(Boolean::valueOf).orElse(false);
                      boolean pretty = ctx.queryParam("pretty").stream().findFirst().map(Boolean::valueOf).orElse(false);

                  try {
                        final Buffer target = transform(ctx.request().getHeader("content-type"), new ByteArrayInputStream(body.getBytes()), acceptableContentType, verbose, pretty);

                        response.setStatusCode(200);
                        response.putHeader("content-type", acceptableContentType);
                        response.end(target);

                      } catch (DocumentParserException e) {

                          response.setStatusCode(400);
                          response.putHeader("content-type", "text/plain");
                          response.end(Buffer.buffer(e.getMessage()));

                      } catch (DocumentWriterException e) {

                          response.setStatusCode(500);
                          response.putHeader("content-type", "text/plain");
                          response.end(Buffer.buffer(e.getMessage()));

                    } catch (Throwable e) {

                        response.setStatusCode(500);
                        response.putHeader("content-type", "text/plain");
                        response.end(Buffer.buffer(e.getMessage()));
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
                .onSuccess(ctx ->
                    System.out.println("Eiger HTTP service started on port " + ctx.actualPort() + ".")
                        )
                .onFailure(ctx ->
                    System.err.println("Eiger HTTP service start failed [" + ctx.getMessage() + "].")
                );
    }

    static final Buffer transform(final String sourceMediaType, final InputStream source, final String targetType, boolean verbose, boolean pretty) throws Exception {

        final DocumentParser parser;

        parser = getParser(sourceMediaType);


        final Document document = parser.parse(null, source);

        if (document == null) {
            return Buffer.buffer();
        }

        final StringWriter target = new StringWriter();

        try (final DocumentWriter writer = getWriter(targetType, pretty, verbose, target)) {
            writer.write(document);
        }

        return Buffer.buffer(target.toString());
    }

    static final DocumentWriter getWriter(final String target, final boolean pretty, final boolean verbose, final Writer writer) throws DocumentWriterException {

        if (Constants.MEDIA_TYPE_ALPS_JSON.equals(target)) {
            return JsonDocumentWriter.create(writer, pretty, verbose);

        } else if (Constants.MEDIA_TYPE_ALPS_XML.equals(target)) {
            return XmlDocumentWriter.create(writer, pretty, verbose);

        } else if (Constants.MEDIA_TYPE_ALPS_YAML.equals(target)) {
            return YamlDocumentWriter.create(writer, verbose);
        }

        throw new IllegalStateException();
    }

    static final DocumentParser getParser(final String mediaType) {

        if (Constants.MEDIA_TYPE_ALPS_JSON.equals(mediaType)) {
            return new JsonDocumentParser();
        }

        if (Constants.MEDIA_TYPE_ALPS_XML.equals(mediaType)) {
            return new XmlDocumentParser();
        }

        if (Constants.MEDIA_TYPE_OPEN_API.equals(mediaType)) {
            return new OpenApiReader();
        }

        throw new IllegalArgumentException("Unsupported source media type [" + mediaType + "].");
    }
    
    static final int getDefaultPort() {
        String envPort = System.getenv("PORT");
        if (envPort != null) {
            return Integer.valueOf(envPort);            
        }
        return 8080;
    }
}
