package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.Post;
import com.ggjuanes.benevity_challenge.server.post.domain.PostBuilder;
import com.ggjuanes.benevity_challenge.server.post.domain.PostReadBuilder;
import com.ggjuanes.benevity_challenge.server.post.domain.PostReadRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(VertxExtension.class)
class FetchPostsTest {
    private final PostReadRepository mockedPostReadRepository = mock(PostReadRepository.class);
    private final FetchPosts fetchPosts = new FetchPosts(mockedPostReadRepository);

    @Test
    void shouldDeleteThePostWhenBelongsToUser(VertxTestContext testContext) {
        // Given
        var expectedPost1 = PostBuilder.random().build();
        var expectedPost2 = PostBuilder.random().build();
        when(mockedPostReadRepository.findAll())
                .thenReturn(
                        Future.succeededFuture(List.of(
                                PostReadBuilder.fromPost(expectedPost1).build(),
                                PostReadBuilder.fromPost(expectedPost2).build()
                        ))
                );

        // When
        fetchPosts.ask().onComplete(testContext.succeeding(postsJson -> {
            // Then
            JsonObject post1 = postsJson.get(0);
            JsonObject post2 = postsJson.get(1);
            assertThat(post1).isEqualTo(PostJsonResponseBuilder.fromPost(expectedPost1).build());
            assertThat(post2).isEqualTo(PostJsonResponseBuilder.fromPost(expectedPost2).build());
            testContext.completeNow();
        }));
    }
}