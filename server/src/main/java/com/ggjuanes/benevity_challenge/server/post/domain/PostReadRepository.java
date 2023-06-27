package com.ggjuanes.benevity_challenge.server.post.domain;


import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Optional;

public interface PostReadRepository {
    Future<List<JsonObject>> findAll();

    Future<Optional<JsonObject>> find(String title);
}
