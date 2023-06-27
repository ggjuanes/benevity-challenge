package com.ggjuanes.benevity_challenge.server.post.application;

import com.ggjuanes.benevity_challenge.server.post.domain.PostBuilder;
import com.ggjuanes.benevity_challenge.server.post.domain.PostRepository;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(VertxExtension.class)
class CreatePostTest {
    private final PostRepository mockedPostRepository = mock(PostRepository.class);
    private final CreatePost createPost = new CreatePost(mockedPostRepository);

    @Test
    void shouldCreateAndStoreANewPost() {
        // Given
        var expectedPost = PostBuilder.random().build();

        // When
        createPost.execute(
                expectedPost.title(),
                expectedPost.content(),
                expectedPost.name(),
                expectedPost.author()
        );

        // Then
        verify(mockedPostRepository).save(expectedPost);
    }
}