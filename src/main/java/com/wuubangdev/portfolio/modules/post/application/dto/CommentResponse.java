package com.wuubangdev.portfolio.modules.post.application.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        String author,
        String content,
        LocalDateTime createdAt,
        Long parentId
) {
}
