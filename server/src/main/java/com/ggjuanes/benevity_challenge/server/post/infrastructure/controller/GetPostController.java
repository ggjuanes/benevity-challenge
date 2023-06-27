package com.ggjuanes.benevity_challenge.server.post.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.post.application.FetchPost;
import com.ggjuanes.benevity_challenge.server.post.domain.PostNotFound;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class GetPostController implements Handler<RoutingContext> {
    private final FetchPost fetchPost;

    private GetPostController(FetchPost fetchPost) {
        this.fetchPost = fetchPost;
    }

    public static GetPostController create(FetchPost fetchPost) {
        return new GetPostController(fetchPost);
    }

    @Override
    public void handle(RoutingContext event) {
        fetchPost.ask(event.request().getParam("title"))
                .onSuccess(post -> {
                    event.response().setStatusCode(200).end(post.encode());
                }).onFailure(err -> {
                    if (err instanceof PostNotFound) {
                        event.response().setStatusCode(404).end();
                    }
                });
    }
}
