package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostTranslationRequest;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest request);
    List<PostResponse> getPublishedPosts();
    List<PostResponse> getAllPosts();
    PostResponse getBySlug(String slug);
    PostResponse getById(Long id);
    PostResponse update(Long id, PostRequest request);
    void delete(Long id);
    PostResponse changeStatus(Long id, String status);
    List<PostResponse> getRecentPosts(int limit);
    List<PostResponse> getRelatedPosts(Long postId, int limit);
    PostEngagementResponse likePost(String slug);
    PostEngagementResponse unlikePost(String slug);
    PostEngagementResponse sharePost(String slug);
    // Pagination
    PageResponse<PostResponse> getPublishedPostsPaged(int page, int size);
    PageResponse<PostResponse> getAllPostsPaged(int page, int size);
    PostResponse upsertTranslation(Long id, String locale, PostTranslationRequest request);
}
