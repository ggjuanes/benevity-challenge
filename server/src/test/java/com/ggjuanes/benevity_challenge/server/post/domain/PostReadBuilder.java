package com.ggjuanes.benevity_challenge.server.post.domain;

import io.vertx.core.json.JsonObject;

public class PostReadBuilder {
    private String title;
    private String content;
    private String name;
    private String author;

    public static PostReadBuilder fromPost(Post post) {
        var postBuilder = new PostReadBuilder();

        postBuilder.title = post.title();
        postBuilder.content = post.content();
        postBuilder.name = post.name();
        postBuilder.author = post.author();
        return postBuilder;
    }

    public JsonObject build() {
        return new JsonObject()
                .put("title", title)
                .put("content", content)
                .put("name", name)
                .put("author", author);
    }
}
