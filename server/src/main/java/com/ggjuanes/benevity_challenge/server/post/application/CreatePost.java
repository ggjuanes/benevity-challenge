package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.Post;
import com.ggjuanes.benevity_challenge.server.post.domain.PostRepository;
import io.vertx.core.Future;

public class CreatePost {
    private final PostRepository postRepository;

    public CreatePost(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // TODO: Replace author by a domain object
    public Future<Void> execute(String title, String content, String name, String author) {
        var post = new Post(
                title,
                content,
                name,
                author
        );

        return postRepository.save(post);
    }
}
