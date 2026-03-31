package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.persistence.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectJpaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    private String category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    @OrderColumn(name = "tag_order")
    private List<String> tags;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_tech_stack", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tech")
    @OrderColumn(name = "tech_order")
    private List<String> techStack;

    private String imageUrl;
    private String projectUrl;
    private String githubUrl;
    private String groupName;
    private Boolean featured;
    private Integer displayOrder;
}
