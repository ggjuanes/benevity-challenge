package com.ggjuanes.benevity_challenge.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(VertxExtension.class)
@Testcontainers
public class TestMainVerticle {
    final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    private MongoClient mongoClient;

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        mongoDBContainer.start();

        mongoClient = MongoClient.create(vertx, new JsonObject()
                .put("connection_string", mongoDBContainer.getConnectionString())
                .put("db_name", "test"));


        System.setProperty("MONGO_DB_CONNECTION_STRING", mongoDBContainer.getConnectionString());
        System.setProperty("MONGO_DB_DATABASE", "test");
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void shouldSignUpANewUser(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        var username = "test";
        var password = "testpw";


        JsonObject query = new JsonObject()
                .put("username", username);
        client.request(HttpMethod.POST, 8888, "127.0.0.1", "/signup").compose(req -> {
                    req.putHeader("content-type", "application/json");
                    return req.send("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
                })
                .onComplete(testContext.succeeding(response -> assertThat(response.statusCode()).isEqualTo(201)))
                .compose(ign -> mongoClient.find("users", query))
                .onComplete(testContext.succeeding(jsonList -> {
                    assertThat(jsonList.get(0).getString("username")).isEqualTo(username);
                    assertThat(jsonList.get(0).getString("password")).isEqualTo(password);
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void shouldFailRequestIfUsernameIsTaken(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        var username = "test";
        var password = "testpw";

        mongoClient
                .save("users", new JsonObject()
                        .put("username", username)
                        .put("password", "fake-pw"))
                .compose(ign -> client.request(HttpMethod.POST, 8888, "127.0.0.1", "/signup"))
                .map(req -> {
                    req.putHeader("content-type", "application/json");
                    return req;
                })
                .compose(req -> req.send("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .onComplete(testContext.succeeding(response -> {
                    assertThat(response.statusCode()).isEqualTo(400);
                    testContext.completeNow();
                }));
    }

    @Test
    void shouldLogIn(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();

        client.request(HttpMethod.POST, 8888, "127.0.0.1", "/login").compose(req -> {
                    req.putHeader("content-type", "application/json");
                    return req.send("{\"username\":\"test\",\"password\":\"test\"}");
                })
                .onComplete(testContext.succeeding(response -> {
                    assertThat(response.statusCode()).isEqualTo(200);
                    testContext.completeNow();
                }));
    }
}
