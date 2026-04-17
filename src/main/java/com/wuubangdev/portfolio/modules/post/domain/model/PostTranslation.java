package com.wuubangdev.portfolio.modules.post.domain.model;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PostTranslation {
    private Long id;
    private Long postId;
    private String locale;
    private String title;
    private String content;
    private String summary;
    private String titleSeo;
    private String descriptionSeo;
    private List<String> seoKeywords;
}
