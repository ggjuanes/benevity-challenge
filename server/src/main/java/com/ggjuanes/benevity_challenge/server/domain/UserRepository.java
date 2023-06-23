package com.ggjuanes.benevity_challenge.server.domain;

import io.vertx.core.Future;

public interface UserRepository {
    Future<Void> save(User user);

    Future<Boolean> exists(String username);
}
