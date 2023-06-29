package com.ggjuanes.benevity_challenge.server.post.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.post.application.CreatePost;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostPostController implements Handler<RoutingContext> {
    private final CreatePost createPost;
    private final Logger logger = LoggerFactory.getLogger(PostPostController.class);

    private PostPostController(CreatePost createPost) {
        this.createPost = createPost;
    }

    public static Handler<RoutingContext> create(CreatePost createPost) {
        return new PostPostController(createPost);
    }

    @Override
    public void handle(RoutingContext event) {
        // TODO: parse body and validate fields
        logger.info("Starting controller");
        JsonObject body = event.body().asJsonObject();
        String title = body.getString("title");
        String content = body.getString("content");
        String name = body.getString("name");

        logger.info("Creating post: {} {} {}", title, content, name);
        createPost
                .execute(
                        title,
                        content,
                        name,
                        event.user().principal().getString("sub")
                )
                .onSuccess(ign -> event.response().setStatusCode(201).end())
                .onFailure(err -> {
                    logger
                            .error("Error creating post", err);
                    event.response().setStatusCode(500).end();
                });
    }
}
