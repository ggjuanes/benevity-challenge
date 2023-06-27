package com.ggjuanes.benevity_challenge.server.user.infrastructure.controller;

import com.ggjuanes.benevity_challenge.server.user.application.SignUpService;
import com.ggjuanes.benevity_challenge.server.user.domain.UsernameAlreadyTaken;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class SignUpController implements Handler<RoutingContext> {
    private final SignUpService signUpService;

    private SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    public static SignUpController create(SignUpService signUpService) {
        return new SignUpController(signUpService);
    }

    @Override
    public void handle(RoutingContext event) {
        JsonObject body = event.body().asJsonObject();
        String username = body.getString("username");
        String password = body.getString("password");

        signUpService.execute(username, password)
                .compose(ok -> event.response().setStatusCode(201).end())
                .onFailure(err -> {
                    if (err instanceof UsernameAlreadyTaken) {
                        event
                                .response()
                                .setStatusCode(400)
                                .end(new JsonObject().put("message", "USERNAME_TAKEN").encode());
                        return;
                    }
                    event.fail(err);
                });
    }
}
