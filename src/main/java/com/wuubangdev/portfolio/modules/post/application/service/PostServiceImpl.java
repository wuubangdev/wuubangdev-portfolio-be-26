package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.mapper.PostApplicationMapper;
import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostTranslationRequest;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import com.wuubangdev.portfolio.modules.post.domain.port.PostRepositoryPort;
import com.wuubangdev.portfolio.modules.post.domain.model.PostTranslation;
import com.wuubangdev.portfolio.modules.post.domain.port.PostTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepositoryPort postRepositoryPort;
    private final PostTranslationRepositoryPort postTranslationRepositoryPort;
    private final PostApplicationMapper postApplicationMapper;
    private final MessageSource messageSource;

    @Override @Transactional
    public PostResponse create(PostRequest request) {
        if (postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException(getMessage("post.slug.exists", request.slug()));
        }
        Post post = postApplicationMapper.toNewDomain(request);
        Post savedPost = postRepositoryPort.save(post);
        syncTranslations(savedPost.getId(), request);
        return toLocalizedResponse(savedPost, resolveLocaleCode());
    }

    @Override
    public List<PostResponse> getPublishedPosts() {
        return toLocalizedResponses(postRepositoryPort.findAllPublished(), resolveLocaleCode());
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return toLocalizedResponses(postRepositoryPort.findAll(), resolveLocaleCode());
    }

    @Override
    public PostResponse getBySlug(String slug) {
        Post post = postRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        return toLocalizedResponse(post, resolveLocaleCode());
    }

    @Override
    public PostResponse getById(Long id) {
        Post post = postRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        return toLocalizedResponse(post, resolveLocaleCode());
    }

    @Override @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post p = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        if (!p.getSlug().equals(request.slug()) && postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException(getMessage("post.slug.exists", request.slug()));
        }
        Post updatedPost = postApplicationMapper.updateDomain(p, request);
        Post savedPost = postRepositoryPort.save(updatedPost);
        syncTranslations(savedPost.getId(), request);
        return toLocalizedResponse(savedPost, resolveLocaleCode());
    }

    @Override
    @Transactional
    public PostResponse upsertTranslation(Long id, String locale, PostTranslationRequest request) {
        Post post = postRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        postTranslationRepositoryPort.save(PostTranslation.builder()
                .postId(id)
                .locale(normalizeLocale(locale))
                .title(request.title())
                .content(request.content())
                .summary(request.summary())
                .titleSeo(request.titleSeo())
                .descriptionSeo(request.descriptionSeo())
                .seoKeywords(request.seoKeywords())
                .build());
        return toLocalizedResponse(post, normalizeLocale(locale));
    }

    @Override @Transactional
    public void delete(Long id) {
        postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        postRepositoryPort.deleteById(id);
        postTranslationRepositoryPort.deleteByPostId(id);
    }

    @Override @Transactional
    public PostResponse changeStatus(Long id, String status) {
        Post post = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        post.setStatus(status);
        return toLocalizedResponse(postRepositoryPort.save(post), resolveLocaleCode());
    }

    @Override
    public List<PostResponse> getRecentPosts(int limit) {
        return toLocalizedResponses(postRepositoryPort.findRecentPosts(limit), resolveLocaleCode());
    }

    @Override
    public List<PostResponse> getRelatedPosts(Long postId, int limit) {
        Post post = postRepositoryPort.findById(postId).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        return toLocalizedResponses(postRepositoryPort.findRelatedPosts(post.getCategory(), postId, limit), resolveLocaleCode());
    }

    @Override
    @Transactional
    public PostEngagementResponse likePost(String slug) {
        Post post = getPublishedPostBySlug(slug);
        post.setLikes(increment(post.getLikes()));
        return postApplicationMapper.toEngagementResponse(postRepositoryPort.save(post));
    }

    @Override
    @Transactional
    public PostEngagementResponse unlikePost(String slug) {
        Post post = getPublishedPostBySlug(slug);
        post.setLikes(decrement(post.getLikes()));
        return postApplicationMapper.toEngagementResponse(postRepositoryPort.save(post));
    }

    @Override
    @Transactional
    public PostEngagementResponse sharePost(String slug) {
        Post post = getPublishedPostBySlug(slug);
        post.setShares(increment(post.getShares()));
        return postApplicationMapper.toEngagementResponse(postRepositoryPort.save(post));
    }

    @Override
    public PageResponse<PostResponse> getPublishedPostsPaged(int page, int size) {
        List<PostResponse> content = toLocalizedResponses(postRepositoryPort.findAllPublishedPaged(page, size), resolveLocaleCode());
        long total = postRepositoryPort.countAllPublished();
        return PageResponse.of(content, page, size, total);
    }

    @Override
    public PageResponse<PostResponse> getAllPostsPaged(int page, int size) {
        List<PostResponse> content = toLocalizedResponses(postRepositoryPort.findAllPaged(page, size), resolveLocaleCode());
        long total = postRepositoryPort.countAll();
        return PageResponse.of(content, page, size, total);
    }

    private String getMessage(String key, Object... args) {
        Locale locale = getCurrentLocale();
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return key; // Fallback to key if message not found
        }
    }

    private Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    private Post getPublishedPostBySlug(String slug) {
        Post post = postRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        if (!Boolean.TRUE.equals(post.getPublished()) || Boolean.TRUE.equals(post.getIsHidden())) {
            throw new ResourceNotFoundException(getMessage("post.not.found"));
        }
        return post;
    }

    private int increment(Integer value) {
        return value == null ? 1 : value + 1;
    }

    private int decrement(Integer value) {
        return value == null || value <= 0 ? 0 : value - 1;
    }

    private void syncTranslations(Long postId, PostRequest request) {
        if (request.translations() == null || request.translations().isEmpty()) {
            return;
        }

        List<PostTranslation> translations = request.translations().stream()
                .map(translation -> PostTranslation.builder()
                        .postId(postId)
                        .locale(normalizeLocale(translation.locale()))
                        .title(translation.title())
                        .content(translation.content())
                        .summary(translation.summary())
                        .titleSeo(translation.titleSeo())
                        .descriptionSeo(translation.descriptionSeo())
                        .seoKeywords(translation.seoKeywords())
                        .build())
                .toList();

        postTranslationRepositoryPort.saveAll(translations);
    }

    private List<PostResponse> toLocalizedResponses(List<Post> posts, String locale) {
        if (posts.isEmpty()) {
            return List.of();
        }

        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, PostTranslation> translationMap = new HashMap<>();
        postTranslationRepositoryPort.findByPostIdInAndLocale(postIds, locale)
                .forEach(translation -> translationMap.put(translation.getPostId(), translation));

        return posts.stream()
                .map(post -> {
                    Post translatedPost = postApplicationMapper.applyTranslation(post, translationMap.get(post.getId()));
                    return postApplicationMapper.toResponse(translatedPost, locale, translationMap.get(post.getId()) != null);
                })
                .toList();
    }

    private PostResponse toLocalizedResponse(Post post, String locale) {
        PostTranslation translation = postTranslationRepositoryPort.findByPostIdAndLocale(post.getId(), locale).orElse(null);
        return postApplicationMapper.toResponse(postApplicationMapper.applyTranslation(post, translation), locale, translation != null);
    }

    private String resolveLocaleCode() {
        return normalizeLocale(getCurrentLocale().getLanguage());
    }

    private String normalizeLocale(String locale) {
        if (locale == null || locale.isBlank()) {
            return "vi";
        }
        return locale.toLowerCase(Locale.ROOT);
    }
}
