package com.ggjuanes.benevity_challenge.server.infrastructure;

import com.ggjuanes.benevity_challenge.server.domain.User;
import com.ggjuanes.benevity_challenge.server.domain.UserRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuthentication;
import io.vertx.ext.auth.mongo.MongoAuthenticationOptions;
import io.vertx.ext.mongo.MongoClient;

public class MongoDbUserService implements UserRepository {
    public static final String USERS_COLLECTION_NAME = "users";
    private final MongoClient mongoClient;

    public MongoDbUserService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Future<Void> save(User user) {
        MongoAuthenticationOptions options = new MongoAuthenticationOptions();
        options.setCollectionName(USERS_COLLECTION_NAME);
        options.setUsernameField("username");
        options.setPasswordField("password");

        MongoAuthentication authenticationProvider =
                MongoAuthentication.create(mongoClient, options);

        String passwordHash = authenticationProvider.hash("sha512", "salt", user.password());

        // @TODO: use serialization instead of getters
        JsonObject document = new JsonObject()
                .put("username", user.username())
                .put("password", passwordHash);
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
