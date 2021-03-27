package com.apicatalog.eiger.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class App extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        final Router router = Router.router(vertx);

        router.route("/").method(HttpMethod.GET).handler(ctx -> ctx.reroute("/index.html"));
        
        router.route()
            .method(HttpMethod.OPTIONS)
            .handler(ctx -> {
                    HttpServerResponse response = ctx.response();
                    response.putHeader("Access-Control-Allow-Origin", "chrome-extension://aejoelaoggembcahagimdiliamlcdmfm");
                    response.end();
                });

        router.route().method(HttpMethod.POST).handler(BodyHandler.create().setBodyLimit(250000));

        router.route()
              .method(HttpMethod.POST)
              .path("/transform")
              .consumes("application/alps+xml")
              .consumes("application/alps+json")
              .consumes("application/oas??")  //TODO
              .produces("application/alps+xml")
              .produces("application/alps+json")
              .produces("application/alps+yaml")

              .handler(ctx -> {

                      Buffer body = ctx.getBody();



                              // This handler will be called for every request
                              HttpServerResponse response = ctx.response();

                              String acceptableContentType = ctx.getAcceptableContentType();

                              response.putHeader("content-type", acceptableContentType);

                              // Write to the response and end it
                              response.end(body);
                          });

//        router.route().handler(
//                CorsHandler
//                    .create()
//                    .allowedMethod(HttpMethod.POST)
//                    .addOrigin("chrome-extension://aejoelaoggembcahagimdiliamlcdmfm")
//                );

        StaticHandler webapp = StaticHandler.create("META-INF/resources")
                .setFilesReadOnly(true)
                .setDirectoryListing(false)
                .setCachingEnabled(false);

        router.route("/*").method(HttpMethod.GET).handler(webapp);
        
        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(8080)
                .onSuccess(ctx ->
                    System.out.println("Eiger HTTP service started on port " + ctx.actualPort() + ".")
                        )
                .onFailure(ctx -> 
                    System.err.println("Eiger HTTP service start failed [" + ctx.getMessage() + "].")
                );
    }
}
