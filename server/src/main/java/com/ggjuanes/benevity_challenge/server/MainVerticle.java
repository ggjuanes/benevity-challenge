package com.ggjuanes.benevity_challenge.server;

import com.ggjuanes.benevity_challenge.server.user.application.LogInService;
import com.ggjuanes.benevity_challenge.server.user.application.SignUpService;
import com.ggjuanes.benevity_challenge.server.user.infrastructure.controller.LogInController;
import com.ggjuanes.benevity_challenge.server.user.infrastructure.persistence.MongoDbUserRepository;
import com.ggjuanes.benevity_challenge.server.user.infrastructure.controller.SignUpController;
import com.ggjuanes.benevity_challenge.server.post.application.CreatePost;
import com.ggjuanes.benevity_challenge.server.post.application.DeletePost;
import com.ggjuanes.benevity_challenge.server.post.application.FetchPost;
import com.ggjuanes.benevity_challenge.server.post.application.FetchPosts;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.controller.DeletePostController;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.controller.GetPostController;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.controller.GetPostsController;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.controller.PostPostController;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.persistence.MongoDbPostReadRepository;
import com.ggjuanes.benevity_challenge.server.post.infrastructure.persistence.MongoDbPostRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.mongo.MongoAuthentication;
import io.vertx.ext.auth.mongo.MongoAuthenticationOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.List;
import java.util.Optional;

import static io.vertx.core.http.HttpMethod.*;

// TODO: when growing, move Main verticle to a 'shared' or service module.
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        var connectionString = getEnv("MONGO_DB_CONNECTION_STRING");
        var database = getEnv("MONGO_DB_DATABASE");

        var mongoClient = MongoClient.create(vertx, new JsonObject()
                .put("connection_string", connectionString)
                .put("db_name", database));
        var authenticationProvider =
                MongoAuthentication.create(mongoClient, new MongoAuthenticationOptions()
                        .setCollectionName(MongoDbUserRepository.USERS_COLLECTION_NAME));
        var provider = JWTAuth.create(vertx, new JWTAuthOptions());
        var authenticationHandler = JWTAuthHandler.create(provider);

        var mongoDbPostReadRepository = new MongoDbPostReadRepository(mongoClient);
        var mongoDbPostRepository = new MongoDbPostRepository(mongoClient);
        var mongoDbUserService = new MongoDbUserRepository(mongoClient);

        RouterBuilder.create(vertx, "server.yaml")
                .map(routerBuilder -> {
                    routerBuilder
                            .operation("signup")
                            .handler(SignUpController.create(new SignUpService(mongoDbUserService)));
                    routerBuilder
                            .operation("login")
                            .handler(LogInController.create(new LogInService(authenticationProvider, provider)));
                    routerBuilder
                            .operation("getPosts")
                            .handler(GetPostsController.create(new FetchPosts(mongoDbPostReadRepository)));
                    routerBuilder
                            .operation("createPost")
                            .handler(PostPostController.create(new CreatePost(mongoDbPostRepository)));
                    routerBuilder
                            .operation("getPost")
                            .handler(GetPostController.create(new FetchPost(mongoDbPostReadRepository)));
                    routerBuilder
                            .operation("deletePost")
                            .handler(DeletePostController.create(new DeletePost(mongoDbPostRepository)));
                    return routerBuilder.createRouter();
                })
                .map(apiRouter -> {
                    Router router = Router.router(vertx);
                    router.route()
                            .handler(CorsHandler.create()
                                    .addOrigins(List.of(
                                            "http://localhost:8000/*",
                                            "http://127.0.0.1:8000/*"
                                    ))
                                    .allowedMethod(GET)
                                    .allowedMethod(POST)
                                    .allowedMethod(DELETE)
                                    .allowedMethod(OPTIONS)
                                    .allowCredentials(true)
                                    .allowedHeader("Access-Control-Allow-Headers")
                                    .allowedHeader("Authorization")
                                    .allowedHeader("Access-Control-Allow-Method")
                                    .allowedHeader("Access-Control-Allow-Origin")
                                    .allowedHeader("Access-Control-Allow-Credentials")
                                    .allowedHeader("Content-Type"));
                    router.route("/posts/*").handler(authenticationHandler);
                    router.route().subRouter(apiRouter);
                    return router;
                })
                .compose(router -> vertx.createHttpServer()
                        .requestHandler(router)
                        .listen(8888))
                .onSuccess(ok -> startPromise.complete())
                .onFailure(startPromise::fail);
    }

    private String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseGet(() -> System.getProperty(key));
    }
}
