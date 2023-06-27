package com.ggjuanes.benevity_challenge.server.post.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.post.application.FetchPosts;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

public class GetPostsController implements Handler<RoutingContext> {
    private final FetchPosts fetchPosts;

    private GetPostsController(FetchPosts fetchPosts) {
        this.fetchPosts = fetchPosts;
    }

    public static GetPostsController create(FetchPosts fetchPosts) {
        return new GetPostsController(fetchPosts);
    }

    @Override
    public void handle(RoutingContext event) {
        fetchPosts
                .ask()
                .onSuccess(posts -> event.response().setStatusCode(200).end(new JsonArray(posts).encode()));
    }
}
