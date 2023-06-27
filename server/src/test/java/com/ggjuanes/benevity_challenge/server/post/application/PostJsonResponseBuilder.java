package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.Post;
import io.vertx.core.json.JsonObject;

public class PostJsonResponseBuilder {
    private String title;
    private String content;
    private String name;
    private String author;

    public static PostJsonResponseBuilder fromPost(Post post) {
        PostJsonResponseBuilder postJsonResponseBuilder = new PostJsonResponseBuilder();
        postJsonResponseBuilder.title = post.title();
        postJsonResponseBuilder.content = post.content();
        postJsonResponseBuilder.name = post.name();
        postJsonResponseBuilder.author = post.author();

        return postJsonResponseBuilder;
    }

    public JsonObject build() {
        return new JsonObject()
                .put("title", title)
                .put("content", content)
                .put("name", name)
                .put("author", author);
    }
}
