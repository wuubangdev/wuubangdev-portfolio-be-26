package com.wuubangdev.portfolio.modules.post.application.mapper;

import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostApplicationMapper {

    public Post toNewDomain(PostRequest request) {
        return Post.builder()
                .title(request.title())
                .slug(request.slug())
                .category(request.category())
                .content(request.content())
                .summary(request.summary())
                .coverImageUrl(request.coverImageUrl())
                .tags(request.tags())
                .published(Boolean.TRUE.equals(request.published()))
                .build();
    }

    public Post updateDomain(Post post, PostRequest request) {
        post.setTitle(request.title());
        post.setSlug(request.slug());
        post.setCategory(request.category());
        post.setContent(request.content());
        post.setSummary(request.summary());
        post.setCoverImageUrl(request.coverImageUrl());
        post.setTags(request.tags());
        post.setPublished(request.published());
        return post;
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getCategory(),
                post.getContent(),
                post.getSummary(),
                post.getCoverImageUrl(),
                post.getTags(),
                post.getPublished()
        );
    }
}
