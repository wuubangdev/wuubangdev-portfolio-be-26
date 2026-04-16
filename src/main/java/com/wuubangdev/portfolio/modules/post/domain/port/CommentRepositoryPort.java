package com.wuubangdev.portfolio.modules.post.domain.port;

import com.wuubangdev.portfolio.modules.post.domain.model.Comment;

import java.util.List;

public interface CommentRepositoryPort {
    Comment save(Comment comment);
    List<Comment> findByPostId(Long postId);
    void deleteById(Long id);
}
