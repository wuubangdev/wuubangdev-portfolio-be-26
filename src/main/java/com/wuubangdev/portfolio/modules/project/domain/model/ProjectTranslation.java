package com.wuubangdev.portfolio.modules.project.domain.model;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectTranslation {
    private Long id;
    private Long projectId;
    private String locale;
    private String title;
    private String description;
    private String content;
    private String titleSeo;
    private String descriptionSeo;
    private List<String> seoKeywords;
}
