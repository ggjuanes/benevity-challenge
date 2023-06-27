package com.ggjuanes.benevity_challenge.server.post.infrastructure.persistence;

import com.ggjuanes.benevity_challenge.server.post.domain.PostReadRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MongoDbPostReadRepository implements PostReadRepository {
    private final MongoClient mongoClient;

    public MongoDbPostReadRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Future<List<JsonObject>> findAll() {
        return mongoClient
                .find("posts", new JsonObject())
                .map(list -> list.stream()
                        .peek(postObject -> postObject.remove("_id")).collect(Collectors.toList()));
    }

    @Override
    public Future<Optional<JsonObject>> find(String title) {
        return mongoClient
                .findOne("posts", new JsonObject().put("title", title), null)
                .map(postObject -> {
                    if (postObject == null) {
                        return null;
                    }

                    postObject.remove("_id");
                    return postObject;
                })
                .map(Optional::ofNullable);
    }
}
