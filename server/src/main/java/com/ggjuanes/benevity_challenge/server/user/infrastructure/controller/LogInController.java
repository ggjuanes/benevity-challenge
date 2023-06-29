package com.ggjuanes.benevity_challenge.server.user.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.user.application.LogInService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class LogInController implements Handler<RoutingContext> {
    private final LogInService logInService;

    private LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    public static LogInController create(LogInService logInService) {
        return new LogInController(logInService);
    }

    @Override
    public void handle(RoutingContext event) {
        // TODO: validate body
        JsonObject body = event.body().asJsonObject();
        String username = body.getJsonObject("post").getString("username");
        String password = body.getJsonObject("post").getString("password");

        logInService.execute(username, password)
                .compose(token -> event.response().setStatusCode(200).end(
                        new JsonObject().put("token", token).encode()
                ))
                .onFailure(err -> event
                        // TODO: Use a domain exception instead any to fail with 400.
                        .response()
                        .setStatusCode(400)
                        .end(new JsonObject().put("message", "AUTHENTICATION_FAILED").encode()));
    }
}
