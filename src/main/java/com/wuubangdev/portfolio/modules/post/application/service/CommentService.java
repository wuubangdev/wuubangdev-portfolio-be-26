package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.modules.post.application.dto.CommentRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByPostId(Long postId);
    CommentResponse addComment(CommentRequest request);
    void deleteComment(Long id);
}
