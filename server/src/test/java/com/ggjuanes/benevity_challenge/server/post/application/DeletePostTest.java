package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.PostBuilder;
import com.ggjuanes.benevity_challenge.server.post.domain.PostDoesNotBelongToUser;
import com.ggjuanes.benevity_challenge.server.post.domain.PostRepository;
import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class DeletePostTest {
    private final PostRepository mockedPostRepository = mock(PostRepository.class);
    private final DeletePost deletePost = new DeletePost(mockedPostRepository);

    @Test
    void shouldDeleteThePostWhenBelongsToUser(VertxTestContext testContext) throws PostDoesNotBelongToUser {
        // Given
        var post = PostBuilder.random().build();
        when(mockedPostRepository.find(post.title()))
                .thenReturn(Future.succeededFuture(Optional.of(post)));
        when(mockedPostRepository.delete(post)).thenReturn(Future.succeededFuture());

        // When
        deletePost.execute(
                post.title(),
                post.author()
        ).onComplete(testContext.succeeding(postJson -> {
            // Then
            verify(mockedPostRepository).delete(post);
            testContext.completeNow();
        }));
    }

    @Test
    void shouldNotDeleteThePostWhenDoesNotBelongToUser(VertxTestContext testContext) {
        // Given
        var post = PostBuilder.random().build();
        when(mockedPostRepository.find(post.title()))
                .thenReturn(Future.succeededFuture(Optional.of(post)));

        // When
        deletePost.execute(
                post.title(),
                "anotherUser"
        ).onFailure(err -> {
            // Then
            assertThat(err).isInstanceOf(PostDoesNotBelongToUser.class);

            testContext.completeNow();
        });
    }
}