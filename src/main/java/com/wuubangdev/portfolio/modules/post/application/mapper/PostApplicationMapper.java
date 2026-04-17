package com.wuubangdev.portfolio.modules.post.application.mapper;

import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.PostTranslation;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostApplicationMapper {

    public Post toNewDomain(PostRequest request) {
        return Post.builder()
                .title(request.title())
                .slug(request.slug())
                .category(request.category()) // tạm thời String
                .content(request.content())
                .summary(request.summary())
                .coverImageUrl(request.coverImageUrl())
                .tags(request.tags())
                .published(Boolean.TRUE.equals(request.published()))
                .author(request.author())
                .titleSeo(defaultIfBlank(request.titleSeo(), request.title()))
                .descriptionSeo(defaultIfBlank(request.descriptionSeo(), request.summary()))
                .thumbnailSeo(defaultIfBlank(request.thumbnailSeo(), request.coverImageUrl()))
                .seoKeywords(request.seoKeywords())
                .canonicalUrl(request.canonicalUrl())
                .indexable(request.indexable() == null ? Boolean.TRUE : request.indexable())
                .likes(0)
                .hearts(0)
                .commentsCount(0)
                .shares(0)
                .status(Boolean.TRUE.equals(request.published()) ? "PUBLISHED" : "DRAFT")
                .displayOrder(request.displayOrder())
                .isHidden(Boolean.TRUE.equals(request.isHidden()))
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
        post.setAuthor(request.author());
        post.setTitleSeo(defaultIfBlank(request.titleSeo(), request.title()));
        post.setDescriptionSeo(defaultIfBlank(request.descriptionSeo(), request.summary()));
        post.setThumbnailSeo(defaultIfBlank(request.thumbnailSeo(), request.coverImageUrl()));
        post.setSeoKeywords(request.seoKeywords());
        post.setCanonicalUrl(request.canonicalUrl());
        post.setIndexable(request.indexable() == null ? Boolean.TRUE : request.indexable());
        post.setStatus(Boolean.TRUE.equals(request.published()) ? "PUBLISHED" : "DRAFT");
        post.setDisplayOrder(request.displayOrder());
        post.setIsHidden(Boolean.TRUE.equals(request.isHidden()));
        return post;
    }

    public PostResponse toResponse(Post post) {
        return toResponse(post, null, false);
    }

    public PostResponse toResponse(Post post, String locale) {
        return toResponse(post, locale, false);
    }

    public PostResponse toResponse(Post post, String locale, boolean translated) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getCategory(), // String
                post.getContent(),
                post.getSummary(),
                post.getCoverImageUrl(),
                post.getTags(),
                post.getPublished(),
                post.getAuthor(),
                post.getTitleSeo(),
                post.getDescriptionSeo(),
                post.getThumbnailSeo(),
                post.getSeoKeywords(),
                post.getCanonicalUrl(),
                post.getIndexable(),
                post.getLikes(),
                post.getHearts(),
                post.getCommentsCount(),
                post.getShares(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getDisplayOrder(),
                post.getIsHidden(),
                locale,
                translated
        );
    }

    public PostEngagementResponse toEngagementResponse(Post post) {
        return new PostEngagementResponse(
                post.getId(),
                post.getSlug(),
                post.getLikes(),
                post.getShares()
        );
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    public Post applyTranslation(Post post, PostTranslation translation) {
        if (translation == null) {
            return post;
        }

        Post translated = Post.builder()
                .id(post.getId())
                .title(translation.getTitle() != null ? translation.getTitle() : post.getTitle())
                .slug(post.getSlug())
                .category(post.getCategory())
                .content(defaultIfBlank(translation.getContent(), post.getContent()))
                .summary(defaultIfBlank(translation.getSummary(), post.getSummary()))
                .coverImageUrl(post.getCoverImageUrl())
                .tags(post.getTags())
                .published(post.getPublished())
                .author(post.getAuthor())
                .titleSeo(defaultIfBlank(translation.getTitleSeo(), post.getTitleSeo()))
                .descriptionSeo(defaultIfBlank(translation.getDescriptionSeo(), post.getDescriptionSeo()))
                .thumbnailSeo(post.getThumbnailSeo())
                .seoKeywords(translation.getSeoKeywords() != null ? translation.getSeoKeywords() : post.getSeoKeywords())
                .canonicalUrl(post.getCanonicalUrl())
                .indexable(post.getIndexable())
                .likes(post.getLikes())
                .hearts(post.getHearts())
                .commentsCount(post.getCommentsCount())
                .shares(post.getShares())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .displayOrder(post.getDisplayOrder())
                .isHidden(post.getIsHidden())
                .build();
        return translated;
    }
}
