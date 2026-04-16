package com.wuubangdev.portfolio.modules.post.application.dto;

public record CommentRequest(
        Long postId,
        String author,
        String content,
        Long parentId
) {
}
