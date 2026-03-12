package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
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

    @Override @Transactional
    public PostResponse create(PostRequest request) {
        if (postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Post post = Post.builder()
                .title(request.title()).slug(request.slug()).content(request.content())
                .summary(request.summary()).coverImageUrl(request.coverImageUrl())
                .tags(request.tags()).published(request.published() != null ? request.published() : false)
                .build();
        return toResponse(postRepositoryPort.save(post));
    }

    @Override
    public List<PostResponse> getPublishedPosts() {
        return postRepositoryPort.findAllPublished().stream().map(this::toResponse).toList();
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PostResponse getBySlug(String slug) {
        return toResponse(postRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug: " + slug)));
    }

    @Override @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post p = postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", id));
        // Check slug unique nếu thay đổi
        if (!p.getSlug().equals(request.slug()) && postRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        p.setTitle(request.title()); p.setSlug(request.slug()); p.setContent(request.content());
        p.setSummary(request.summary()); p.setCoverImageUrl(request.coverImageUrl());
        p.setTags(request.tags()); p.setPublished(request.published());
        return toResponse(postRepositoryPort.save(p));
    }

    @Override @Transactional
    public void delete(Long id) {
        postRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", id));
        postRepositoryPort.deleteById(id);
    }

    private PostResponse toResponse(Post p) {
        return new PostResponse(p.getId(), p.getTitle(), p.getSlug(), p.getContent(),
                p.getSummary(), p.getCoverImageUrl(), p.getTags(), p.getPublished());
    }
}
