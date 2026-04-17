package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.mapper.PostApplicationMapper;
import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import com.wuubangdev.portfolio.modules.post.domain.port.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepositoryPort postRepositoryPort;
    private final PostApplicationMapper postApplicationMapper;
    private final MessageSource messageSource;

    @Override @Transactional
    public PostResponse create(PostRequest request) {
        if (postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException(getMessage("post.slug.exists", request.slug()));
        }
        Post post = postApplicationMapper.toNewDomain(request);
        return postApplicationMapper.toResponse(postRepositoryPort.save(post));
    }

    @Override
    public List<PostResponse> getPublishedPosts() {
        return postRepositoryPort.findAllPublished().stream().map(postApplicationMapper::toResponse).toList();
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepositoryPort.findAll().stream().map(postApplicationMapper::toResponse).toList();
    }

    @Override
    public PostResponse getBySlug(String slug) {
        return postApplicationMapper.toResponse(postRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found"))));
    }

    @Override
    public PostResponse getById(Long id) {
        return postApplicationMapper.toResponse(postRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found"))));
    }

    @Override @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post p = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        if (!p.getSlug().equals(request.slug()) && postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException(getMessage("post.slug.exists", request.slug()));
        }
        Post updatedPost = postApplicationMapper.updateDomain(p, request);
        return postApplicationMapper.toResponse(postRepositoryPort.save(updatedPost));
    }

    @Override @Transactional
    public void delete(Long id) {
        postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        postRepositoryPort.deleteById(id);
    }

    @Override @Transactional
    public PostResponse changeStatus(Long id, String status) {
        Post post = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        post.setStatus(status);
        return postApplicationMapper.toResponse(postRepositoryPort.save(post));
    }

    @Override
    public List<PostResponse> getRecentPosts(int limit) {
        return postRepositoryPort.findRecentPosts(limit).stream().map(postApplicationMapper::toResponse).toList();
    }

    @Override
    public List<PostResponse> getRelatedPosts(Long postId, int limit) {
        Post post = postRepositoryPort.findById(postId).orElseThrow(() -> new ResourceNotFoundException(getMessage("post.not.found")));
        return postRepositoryPort.findRelatedPosts(post.getCategory(), postId, limit).stream().map(postApplicationMapper::toResponse).toList();
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
        List<PostResponse> content = postRepositoryPort.findAllPublishedPaged(page, size).stream().map(postApplicationMapper::toResponse).toList();
        long total = postRepositoryPort.countAllPublished();
        return PageResponse.of(content, page, size, total);
    }

    @Override
    public PageResponse<PostResponse> getAllPostsPaged(int page, int size) {
        List<PostResponse> content = postRepositoryPort.findAllPaged(page, size).stream().map(postApplicationMapper::toResponse).toList();
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
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return (Locale) attrs.getRequest().getSession().getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
        } catch (Exception e) {
            return Locale.getDefault();
        }
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
}
