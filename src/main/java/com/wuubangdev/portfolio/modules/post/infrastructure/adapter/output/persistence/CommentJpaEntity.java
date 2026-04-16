package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentJpaEntity extends BaseEntity {
    private Long postId;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private Long parentId;
}
