package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.mapper.PostApplicationMapper;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import com.wuubangdev.portfolio.modules.post.domain.port.PostRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceImplTest {

    private InMemoryPostRepository postRepositoryPort;
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postRepositoryPort = new InMemoryPostRepository();
        postService = new PostServiceImpl(postRepositoryPort, new PostApplicationMapper(), messageSource());
    }

    @Test
    void likePostShouldIncreaseLikes() {
        Post post = publishedPost();
        post.setLikes(2);
        postRepositoryPort.post = post;

        PostEngagementResponse response = postService.likePost("hello-world");

        assertThat(postRepositoryPort.savedPost.getLikes()).isEqualTo(3);
        assertThat(response.likes()).isEqualTo(3);
        assertThat(response.slug()).isEqualTo("hello-world");
    }

    @Test
    void unlikePostShouldNotGoBelowZero() {
        Post post = publishedPost();
        post.setLikes(0);
        postRepositoryPort.post = post;

        PostEngagementResponse response = postService.unlikePost("hello-world");

        assertThat(postRepositoryPort.savedPost.getLikes()).isZero();
        assertThat(response.likes()).isZero();
    }

    @Test
    void sharePostShouldIncreaseSharesWhenNull() {
        Post post = publishedPost();
        post.setShares(null);
        postRepositoryPort.post = post;

        PostEngagementResponse response = postService.sharePost("hello-world");

        assertThat(postRepositoryPort.savedPost.getShares()).isEqualTo(1);
        assertThat(response.shares()).isEqualTo(1);
    }

    private MessageSource messageSource() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("post.not.found", Locale.getDefault(), "Post not found");
        return messageSource;
    }

    private Post publishedPost() {
        return Post.builder()
                .id(1L)
                .slug("hello-world")
                .published(true)
                .isHidden(false)
                .build();
    }

    private static class InMemoryPostRepository implements PostRepositoryPort {
        private Post post;
        private Post savedPost;

        @Override
        public Post save(Post post) {
            this.savedPost = post;
            this.post = post;
            return post;
        }

        @Override
        public List<Post> findAllPublished() {
            return List.of();
        }

        @Override
        public List<Post> findAll() {
            return List.of();
        }

        @Override
        public Optional<Post> findById(Long id) {
            return Optional.ofNullable(post);
        }

        @Override
        public Optional<Post> findBySlug(String slug) {
            return Optional.ofNullable(post).filter(value -> slug.equals(value.getSlug()));
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public boolean existsBySlug(String slug) {
            return false;
        }

        @Override
        public List<Post> findRecentPosts(int limit) {
            return List.of();
        }

        @Override
        public List<Post> findRelatedPosts(String category, Long excludeId, int limit) {
            return List.of();
        }

        @Override
        public List<Post> findAllPublishedPaged(int page, int size) {
            return List.of();
        }

        @Override
        public long countAllPublished() {
            return 0;
        }

        @Override
        public List<Post> findAllPaged(int page, int size) {
            return List.of();
        }

        @Override
        public long countAll() {
            return 0;
        }
    }
}
