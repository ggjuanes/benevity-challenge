package com.ggjuanes.benevity_challenge.server.post.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.post.application.DeletePost;
import com.ggjuanes.benevity_challenge.server.post.domain.PostDoesNotBelongToUser;
import com.ggjuanes.benevity_challenge.server.post.domain.PostNotFound;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeletePostController implements Handler<RoutingContext> {
    private final DeletePost deletePost;
    private final Logger logger = LoggerFactory.getLogger(DeletePostController.class);

    private DeletePostController(DeletePost deletePost) {
        this.deletePost = deletePost;
    }

    public static Handler<RoutingContext> create(DeletePost deletePost) {
        return new DeletePostController(deletePost);
    }

    @Override
    public void handle(RoutingContext event) {
        String title = event.request().getParam("title");

        deletePost
                .execute(
                        title,
                        event.user().principal().getString("sub")
                ).onSuccess(ign -> event.response().setStatusCode(200).end())
                .onFailure(err -> {
                    if (err instanceof PostNotFound) {
                        event.response().setStatusCode(404).end();
                    } else if (err instanceof PostDoesNotBelongToUser) {
                        event.response().setStatusCode(403).end();
                    }
                    logger.error("Error deleting post", err);
                });
    }
}
