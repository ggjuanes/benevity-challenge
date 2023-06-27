package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class FetchPostTest {
    private final PostReadRepository mockedPostReadRepository = mock(PostReadRepository.class);
    private final FetchPost fetchPost = new FetchPost(mockedPostReadRepository);

    @Test
    void shouldFindPost(VertxTestContext testContext) {
        // Given
        var post = PostBuilder.random().build();
        when(mockedPostReadRepository.find(post.title()))
                .thenReturn(
                        Future.succeededFuture(
                                Optional.of(PostReadBuilder.fromPost(post).build()))
                );

        // When
        fetchPost.ask(post.title()).onComplete(testContext.succeeding(postJson -> {
            // Then
            assertThat(postJson).isEqualTo(PostJsonResponseBuilder.fromPost(post).build());
            testContext.completeNow();
        }));
    }

    @Test
    void shouldThrowExceptionIfPostCannotBeFound(VertxTestContext testContext) {
        // Given
        var post = PostBuilder.random().build();
        when(mockedPostReadRepository.find(post.title()))
                .thenReturn(Future.succeededFuture(Optional.empty()));

        // When
        fetchPost.ask(post.title()).onComplete(testContext.failing(throwable -> {
            // Then
            assertThat(throwable).isInstanceOf(PostNotFound.class);
            testContext.completeNow();
        }));
    }
}