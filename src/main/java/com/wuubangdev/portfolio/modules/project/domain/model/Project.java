package com.wuubangdev.portfolio.modules.project.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Project {
    private Long id;
    private String title;
    private String slug;
    private String category;
    private List<String> tags;
    private String description;
    private String content;
    private List<String> techStack;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
    private String groupName;
    private Boolean featured;
    private Integer displayOrder;
    private String titleSeo;
    private String descriptionSeo;
    private String thumbnailSeo;
    private List<String> seoKeywords;
    private String canonicalUrl;
    private Boolean indexable;
    private LocalDateTime createdAt;
}
