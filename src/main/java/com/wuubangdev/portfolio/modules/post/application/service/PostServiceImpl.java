package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.post.application.mapper.PostApplicationMapper;
import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import com.wuubangdev.portfolio.modules.post.domain.port.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepositoryPort postRepositoryPort;
    private final PostApplicationMapper postApplicationMapper;

    @Override @Transactional
    public PostResponse create(PostRequest request) {
        if (postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
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
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug: " + slug)));
    }

    @Override
    public PostResponse getById(Long id) {
        return postApplicationMapper.toResponse(postRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id)));
    }

    @Override @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post p = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", id));
        if (!p.getSlug().equals(request.slug()) && postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Post updatedPost = postApplicationMapper.updateDomain(p, request);
        return postApplicationMapper.toResponse(postRepositoryPort.save(updatedPost));
    }

    @Override @Transactional
    public void delete(Long id) {
        postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", id));
        postRepositoryPort.deleteById(id);
    }

    @Override @Transactional
    public PostResponse changeStatus(Long id, String status) {
        Post post = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", id));
        post.setStatus(status);
        return postApplicationMapper.toResponse(postRepositoryPort.save(post));
    }

    @Override
    public List<PostResponse> getRecentPosts(int limit) {
        return postRepositoryPort.findRecentPosts(limit).stream().map(postApplicationMapper::toResponse).toList();
    }

    @Override
    public List<PostResponse> getRelatedPosts(Long postId, int limit) {
        Post post = postRepositoryPort.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        return postRepositoryPort.findRelatedPosts(post.getCategory(), postId, limit).stream().map(postApplicationMapper::toResponse).toList();
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
}
