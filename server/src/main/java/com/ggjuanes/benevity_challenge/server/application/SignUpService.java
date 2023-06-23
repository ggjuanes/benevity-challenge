package com.ggjuanes.benevity_challenge.server.application;

import com.ggjuanes.benevity_challenge.server.domain.User;
import com.ggjuanes.benevity_challenge.server.domain.UserRepository;
import com.ggjuanes.benevity_challenge.server.domain.UsernameTaken;
import io.vertx.core.Future;

public class SignUpService {
    private final UserRepository userRepository;

    public SignUpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Future<Void> execute(String username, String password) {
        // TODO: Check if username is valid
        // TODO: Check if password is valid
        // TODO: Hash password
        return userRepository
                .exists(username)
                .map(exists -> {
                    if (exists) {
                        throw new UsernameTaken();
                    }
                    return null;
                })
                .compose(ign -> userRepository.save(User.create(username, password)));
    }
}
