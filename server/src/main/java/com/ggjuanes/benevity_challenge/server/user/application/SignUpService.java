package com.ggjuanes.benevity_challenge.server.user.application;

import com.ggjuanes.benevity_challenge.server.user.domain.User;
import com.ggjuanes.benevity_challenge.server.user.domain.UserRepository;
import com.ggjuanes.benevity_challenge.server.user.domain.UsernameAlreadyTaken;
import io.vertx.core.Future;

public class SignUpService {
    private final UserRepository userRepository;

    public SignUpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Future<Void> execute(String username, String password) {
        // TODO: Check if username is valid
        // TODO: Check if password is valid
        return userRepository
                .exists(username)
                .map(exists -> {
                    if (exists) {
                        throw new UsernameAlreadyTaken();
                    }
                    return null;
                })
                .compose(ign -> userRepository.save(User.create(username, password)));
    }
}
