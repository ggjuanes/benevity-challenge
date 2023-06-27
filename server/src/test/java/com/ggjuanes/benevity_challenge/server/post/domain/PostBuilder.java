package com.ggjuanes.benevity_challenge.server.post.domain;

import org.apache.commons.lang3.RandomStringUtils;

public class PostBuilder {
    private String title;
    private String content;
    private String name;
    private String author;

    public static PostBuilder random() {
        var postBuilder = new PostBuilder();
        postBuilder.title = RandomStringUtils.randomAlphanumeric(10);
        postBuilder.content = RandomStringUtils.randomAlphanumeric(100);
        postBuilder.name = RandomStringUtils.randomAlphanumeric(10);
        postBuilder.author = RandomStringUtils.randomAlphanumeric(10);
        return postBuilder;
    }

    public Post build() {
        return new Post(title, content, name, author);
    }

    public PostBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }
}
