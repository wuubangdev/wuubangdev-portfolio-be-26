package com.wuubangdev.portfolio.modules.project.domain.model;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Project {
    private Long id;
    private String title;
    private String description;
    private List<String> techStack;
    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
    private String groupName;
    private Boolean featured;
    private Integer displayOrder;
}
