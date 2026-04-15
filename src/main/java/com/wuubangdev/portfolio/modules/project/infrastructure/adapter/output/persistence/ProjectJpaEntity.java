package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectJpaEntity extends BaseEntity {
    private String title;

    @Indexed(unique = true)
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
}
