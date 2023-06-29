package com.ggjuanes.benevity_challenge.server.user.application;

import com.ggjuanes.benevity_challenge.server.user.domain.User;
import com.ggjuanes.benevity_challenge.server.user.domain.UserRepository;
import com.ggjuanes.benevity_challenge.server.user.domain.UsernameAlreadyTaken;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(SignUpService.class);

    public SignUpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Future<Void> execute(String username, String password) {
        // TODO: Check if username is valid
        // TODO: Check if password is valid
        logger.info("Creating user {}, {}", username, password);
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
