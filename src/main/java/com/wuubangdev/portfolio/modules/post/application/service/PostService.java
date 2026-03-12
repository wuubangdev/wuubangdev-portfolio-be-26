package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest request);
    List<PostResponse> getPublishedPosts();
    List<PostResponse> getAllPosts();
    PostResponse getBySlug(String slug);
    PostResponse update(Long id, PostRequest request);
    void delete(Long id);
}
