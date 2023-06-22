package com.ggjuanes.benevity_challenge.server;

import com.ggjuanes.benevity_challenge.server.application.SignUpService;
import com.ggjuanes.benevity_challenge.server.infrastructure.MongoDbUserService;
import com.ggjuanes.benevity_challenge.server.infrastructure.SignUpController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import java.util.Optional;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
        var connectionString = Optional.ofNullable(System.getenv("MONGO_DB_CONNECTION_STRING"))
                .orElseGet(() -> System.getProperty("MONGO_DB_CONNECTION_STRING"));
        var database = Optional.ofNullable(System.getenv("MONGO_DB_DATABASE"))
                .orElseGet(() -> System.getProperty("MONGO_DB_DATABASE"));

        var signUpService = new SignUpService(new MongoDbUserService(vertx, connectionString, database));
        RouterBuilder.create(vertx, "server.yaml")
                .map(routerBuilder -> {
                    routerBuilder
                            .operation("signup")
                            .handler(SignUpController.create(signUpService));
                    routerBuilder
                            .operation("login")
                            .handler(routingContext -> {
                                routingContext.response().setStatusCode(200).end();
                            });

                    return routerBuilder;
                })
                .map(routerBuilder -> {
                    var routerApi = routerBuilder.createRouter();
                    Router router = Router.router(vertx);
                    router.route()
                            .handler(CorsHandler.create()
                                    .addOrigin("http://127.0.0.1:8000/*")
                                    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                                    .allowCredentials(true)
                                    .allowedHeader("Access-Control-Allow-Headers")
                                    .allowedHeader("Authorization")
                                    .allowedHeader("Access-Control-Allow-Method")
                                    .allowedHeader("Access-Control-Allow-Origin")
                                    .allowedHeader("Access-Control-Allow-Credentials")
                                    .allowedHeader("Content-Type"));
                    router.route().subRouter(routerApi);
                    return router;
                })
                .compose(router -> vertx.createHttpServer()
                        .requestHandler(router)
                        .listen(8888))
                .onSuccess(ok -> startPromise.complete())
                .onFailure(startPromise::fail);
    }
}
