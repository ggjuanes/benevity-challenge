package com.ggjuanes.benevity_challenge.server.post.infrastructure.persistence;

import com.ggjuanes.benevity_challenge.server.post.domain.Post;
import com.ggjuanes.benevity_challenge.server.post.domain.PostRepository;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Optional;

public class MongoDbPostRepository implements PostRepository {
    private final MongoClient mongoClient;

    public MongoDbPostRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Future<Void> save(Post post) {
        return mongoClient
                .save("posts", JsonObject.mapFrom(post))
                .mapEmpty();
    }

    @Override
    public Future<Void> delete(Post post) {
        return mongoClient
                .removeDocument("posts", new JsonObject().put("title", post.title()))
                .mapEmpty();
    }

    @Override
    public Future<Optional<Post>> find(String title) {
        return mongoClient
                .findOne("posts", new JsonObject().put("title", title), null)
                .map(json -> json == null ? null : Json.decodeValue(json.encode(), Post.class))
                .map(Optional::ofNullable);
    }
}
