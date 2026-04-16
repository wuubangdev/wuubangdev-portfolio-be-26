package com.wuubangdev.portfolio.modules.post.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Comment {
    private Long id;
    private Long postId;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private Long parentId; // for nested comments
}
