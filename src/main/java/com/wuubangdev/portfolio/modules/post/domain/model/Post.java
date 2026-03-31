package com.wuubangdev.portfolio.modules.post.domain.model;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String slug;
    private String category;
    private String content; // markdown
    private String summary;
    private String coverImageUrl;
    private List<String> tags;
    private Boolean published;
}
