package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.PostReadRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class FetchPosts {
    private final PostReadRepository postReadRepository;

    public FetchPosts(PostReadRepository postReadRepository) {
        this.postReadRepository = postReadRepository;
    }

    public Future<List<JsonObject>> ask() {
        return postReadRepository.findAll();
    }
}
