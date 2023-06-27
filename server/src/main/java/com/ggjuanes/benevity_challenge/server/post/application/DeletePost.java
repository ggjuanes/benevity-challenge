package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.PostDoesNotBelongToUser;
import com.ggjuanes.benevity_challenge.server.post.domain.PostNotFound;
import com.ggjuanes.benevity_challenge.server.post.domain.PostRepository;
import io.vertx.core.Future;

public class DeletePost {
    private final PostRepository postRepository;

    public DeletePost(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Future<Void> execute(String title, String user) {
        return postRepository
                .find(title)
                .map(post -> {
                    if (post.isEmpty()) {
                        throw new PostNotFound();
                    }

                    if (!post.get().belongsTo(user)) {
                        throw new PostDoesNotBelongToUser();
                    }
                    return post.get();
                })
                .compose(postRepository::delete);
    }
}
