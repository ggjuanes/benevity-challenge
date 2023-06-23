package com.ggjuanes.benevity_challenge.server.infrastructure;

import com.ggjuanes.benevity_challenge.server.domain.User;
import com.ggjuanes.benevity_challenge.server.domain.UserRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoDbUserService implements UserRepository {
    private final MongoClient mongoClient;

    public MongoDbUserService(Vertx vertx, String connetionString, String database) {
        System.out.println(connetionString);
        System.out.println(database);
        mongoClient = MongoClient.create(vertx, new JsonObject()
                .put("connection_string", connetionString)
                .put("db_name", database));
    }

    @Override
    public Future<Void> save(User user) {
        // @TODO: use serialization instead of getters
        JsonObject document = new JsonObject()
                .put("username", user.username())
                .put("password", user.password());
        return mongoClient.save("users", document)
                .mapEmpty();
    }

    @Override
    public Future<Boolean> exists(String username) {
        return mongoClient
                .find("users", new JsonObject().put("username", username))
                .map(users -> !users.isEmpty());
    }
}
