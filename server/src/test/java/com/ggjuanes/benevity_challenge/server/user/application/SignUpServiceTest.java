package com.ggjuanes.benevity_challenge.server.user.application;

import com.ggjuanes.benevity_challenge.server.user.application.SignUpService;
import com.ggjuanes.benevity_challenge.server.user.domain.User;
import com.ggjuanes.benevity_challenge.server.user.domain.UserRepository;
import com.ggjuanes.benevity_challenge.server.user.domain.UsernameAlreadyTaken;
import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class SignUpServiceTest {
    private final UserRepository mockedUserRepository = mock(UserRepository.class);
    private final SignUpService signUpService = new SignUpService(mockedUserRepository);
    @Test
    void shouldCreateUser() {
        // Given
        var username = "test";
        var password = "testpw";

        // When
        when(mockedUserRepository.exists(username))
                .thenReturn(Future.succeededFuture(false));
        when(mockedUserRepository.save(any()))
                .thenReturn(Future.succeededFuture());
        signUpService.execute(username, password);

        // Then
        verify(mockedUserRepository).save(User.create(username, password));
    }

    @Test
    void shouldNotCreateUserWhenUsernameAlreadyExists(VertxTestContext testContext) {
        // Given
        var usernameAlreadyExists = "test";
        var password = "testpw";
        when(mockedUserRepository.exists(usernameAlreadyExists))
                .thenReturn(Future.succeededFuture(true));

        // When
        signUpService
                .execute(usernameAlreadyExists, password)
                .onFailure(err -> {
                    assertThat(err).isInstanceOf(UsernameAlreadyTaken.class);
                    // Then
                    verify(mockedUserRepository, never()).save(any());

                    testContext.completeNow();
                });
    }
}