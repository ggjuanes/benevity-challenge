package com.ggjuanes.benevity_challenge.server.post.domain;

import io.vertx.core.Future;

import java.util.Optional;

public interface PostRepository {
    Future<Void> save(Post post);

    Future<Void> delete(Post post);

    Future<Optional<Post>> find(String title);
}
