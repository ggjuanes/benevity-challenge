package com.ggjuanes.benevity_challenge.server.post.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private final String title;
    private final String content;
    private final String name;
    private final String author;

    public Post(@JsonProperty("title") String title, @JsonProperty("content") String content, @JsonProperty("name") String name, @JsonProperty("author") String author) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.author = author;
    }

    @JsonGetter("title")
    public String title() {
        return title;
    }

    @JsonGetter("content")
    public String content() {
        return content;
    }

    @JsonGetter("name")
    public String name() {
        return name;
    }

    @JsonGetter("author")
    public String author() {
        return author;
    }

    public boolean belongsTo(String author) {
        return this.author.equals(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(content, post.content) && Objects.equals(name, post.name) && Objects.equals(author, post.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, name, author);
    }
}
