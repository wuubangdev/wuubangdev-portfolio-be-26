package com.wuubangdev.portfolio.modules.post.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String slug;
    private String category; // giữ String
    private String content; // markdown
    private String summary;
    private String coverImageUrl;
    private List<String> tags;
    private Boolean published;
    // Thêm mới
    private String author;
    private String titleSeo;
    private String descriptionSeo;
    private String thumbnailSeo;
    private List<String> seoKeywords;
    private String canonicalUrl;
    private Boolean indexable;
    private Integer likes;
    private Integer hearts;
    private Integer commentsCount;
    private Integer shares;
    private String status;
    private LocalDateTime createdAt;
    private Integer displayOrder;
    private Boolean isHidden;
}
