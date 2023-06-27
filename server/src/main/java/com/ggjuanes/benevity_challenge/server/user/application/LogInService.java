package com.ggjuanes.benevity_challenge.server.user.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.auth.jwt.JWTAuth;

public class LogInService {
    private final AuthenticationProvider passwordAuthenticationProvider;
    private final JWTAuth jwtAuth;

    public LogInService(AuthenticationProvider passwordAuthenticationProvider, JWTAuth jwtAuth) {
        this.passwordAuthenticationProvider = passwordAuthenticationProvider;
        this.jwtAuth = jwtAuth;
    }
    public Future<String> execute(String username, String password) {
        // TODO: configure JWT expiration time and keys.
        return passwordAuthenticationProvider.authenticate(new UsernamePasswordCredentials(username, password))
                .map(user -> jwtAuth.generateToken(
                        // TODO: add claims to the token. Consider user serialization
                        new JsonObject().put("sub", username), new JWTOptions()
                ));
    }
}
