package com.ggjuanes.benevity_challenge.server.post;

import com.ggjuanes.benevity_challenge.server.MainVerticle;
import com.ggjuanes.benevity_challenge.server.post.application.PostJsonResponseBuilder;
import com.ggjuanes.benevity_challenge.server.post.domain.Post;
import com.ggjuanes.benevity_challenge.server.post.domain.PostBuilder;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
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
public class TestIntegrationPost {
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjg3ODAzNDU5fQ";
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
    void shouldReturnAllPost(Vertx vertx, VertxTestContext testContext) {
        var post = PostBuilder.random().build();
        givenASavedPost(post)
                .compose(ign -> prepareRequest(vertx, HttpMethod.GET, "/posts"))
                .compose(HttpClientRequest::send)
                .onComplete(testContext.succeeding(response -> assertThat(response.statusCode()).isEqualTo(200)))
                .compose(HttpClientResponse::body)
                .map(Buffer::toJsonArray)
                .onComplete(testContext.succeeding(json -> {
                    assertThat(json.getJsonObject(0)).isEqualTo(PostJsonResponseBuilder.fromPost(post).build());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }



    @Test
    void shouldReturn404WhenPostIsNotFound(Vertx vertx, VertxTestContext testContext) {
        var title = "test";

        prepareRequest(vertx, HttpMethod.GET, "/posts/" + title)
                .compose(HttpClientRequest::send)
                .onComplete(testContext.succeeding(response -> {
                    assertThat(response.statusCode()).isEqualTo(404);
                    testContext.completeNow();
                }));
    }

    @Test
    void shouldReturnPost(Vertx vertx, VertxTestContext testContext) {
        var post = PostBuilder.random().build();
        givenASavedPost(post)
                .compose(ign -> prepareRequest(vertx, HttpMethod.GET, "/posts/" + post.title()))
                .compose(HttpClientRequest::send)
                .onComplete(testContext.succeeding(response -> assertThat(response.statusCode()).isEqualTo(200)))
                .compose(HttpClientResponse::body)
                .map(Buffer::toJsonObject)
                .onComplete(testContext.succeeding(json -> {
                    assertThat(json).isEqualTo(PostJsonResponseBuilder.fromPost(post).build());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void shouldNotAuthorizeDeleteIfUserIsNotAuthor(Vertx vertx, VertxTestContext testContext) {
        // Given
        var post = PostBuilder.random().build();

        givenASavedPost(post)
                // When
                .compose(ign -> prepareRequest(vertx, HttpMethod.DELETE, "/posts/" + post.title()))
                .compose(HttpClientRequest::send)
                // Then
                .onComplete(testContext.succeeding(response -> {
                    assertThat(response.statusCode()).isEqualTo(403);
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void shouldDeletePost(Vertx vertx, VertxTestContext testContext) {
        // Given
        var post = PostBuilder
                .random()
                .withAuthor("test")
                .build();

        // When
        givenASavedPost(post)
                .compose(ign -> prepareRequest(vertx, HttpMethod.DELETE, "/posts/" + post.title()))
                .compose(HttpClientRequest::send)
                // Then
                .onComplete(testContext.succeeding(response -> assertThat(response.statusCode()).isEqualTo(200)))
                .compose(ign -> mongoClient.find("posts", new JsonObject().put("title", post.title())))
                .onComplete(testContext.succeeding(jsonList -> {
                    assertThat(jsonList).isEmpty();
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void shouldReturnNotFoundIfPostDoesNotExist(Vertx vertx, VertxTestContext testContext) {
        // Given
        var post = PostBuilder
                .random()
                .withAuthor("test")
                .build();

        // When
        prepareRequest(vertx, HttpMethod.DELETE, "/posts/" + post.title())
                .compose(HttpClientRequest::send)
                // Then
                .onComplete(testContext.succeeding(response -> {
                    assertThat(response.statusCode()).isEqualTo(404);
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void shouldPersistANewPost(Vertx vertx, VertxTestContext testContext) {
        // Given
        var post = PostBuilder
                .random()
                .withAuthor("test")
                .build();

        // When
        prepareRequest(vertx, HttpMethod.POST, "/posts")
                .compose(req -> req.send(new JsonObject()
                        .put("title", post.title())
                        .put("content", post.content())
                        .put("name", post.name())
                        .encode()))
                // Then
                .onComplete(testContext.succeeding(response -> assertThat(response.statusCode()).isEqualTo(201)))
                .compose(ign -> mongoClient.findOne("posts", new JsonObject().put("title", post.title()), null))
                .onComplete(testContext.succeeding(json -> {
                    assertThat(json.getString("title")).isEqualTo(post.title());
                    assertThat(json.getString("content")).isEqualTo(post.content());
                    assertThat(json.getString("author")).isEqualTo(post.author());
                    assertThat(json.getString("name")).isEqualTo(post.name());
                    testContext.completeNow();
                }));
    }

    private Future<Void> givenASavedPost(Post post) {
        return mongoClient
                .save("posts", JsonObject.mapFrom(post))
                .mapEmpty();
    }

    private Future<HttpClientRequest> prepareRequest(Vertx vertx, HttpMethod method, String path) {
        HttpClient client = vertx.createHttpClient();

        return client.request(method, 8888, "127.0.0.1", path)
                .map(req -> {
                    req.putHeader("content-type", "application/json");
                    req.putHeader("Authorization", "Bearer " + TOKEN);
                    return req;
                });
    }
}
