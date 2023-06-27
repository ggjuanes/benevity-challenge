package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.PostNotFound;
import com.ggjuanes.benevity_challenge.server.post.domain.PostReadRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class FetchPost {
    private final PostReadRepository postReadRepository;

    public FetchPost(PostReadRepository postReadRepository) {
        this.postReadRepository = postReadRepository;
    }

    public Future<JsonObject> ask(String title) {
        return postReadRepository.find(title).map(post -> {
            if (post.isEmpty()) {
                throw new PostNotFound();
            }
            return post.get();
        });
    }
}
