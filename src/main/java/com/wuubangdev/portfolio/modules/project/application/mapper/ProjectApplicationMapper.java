package com.wuubangdev.portfolio.modules.project.application.mapper;

import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectApplicationMapper {

    public Project toNewDomain(ProjectRequest request) {
        return Project.builder()
                .title(request.title())
                .slug(request.slug())
                .category(request.category())
                .tags(request.tags())
                .description(request.description())
                .content(request.content())
                .techStack(request.techStack())
                .imageUrl(request.imageUrl())
                .projectUrl(request.projectUrl())
                .githubUrl(request.githubUrl())
                .groupName(request.groupName())
                .featured(Boolean.TRUE.equals(request.featured()))
                .displayOrder(request.displayOrder())
                .build();
    }

    public Project updateDomain(Project project, ProjectRequest request) {
        project.setTitle(request.title());
        project.setSlug(request.slug());
        project.setCategory(request.category());
        project.setTags(request.tags());
        project.setDescription(request.description());
        project.setContent(request.content());
        project.setTechStack(request.techStack());
        project.setImageUrl(request.imageUrl());
        project.setProjectUrl(request.projectUrl());
        project.setGithubUrl(request.githubUrl());
        project.setGroupName(request.groupName());
        project.setFeatured(request.featured());
        project.setDisplayOrder(request.displayOrder());
        return project;
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getSlug(),
                project.getCategory(),
                project.getTags(),
                project.getDescription(),
                project.getContent(),
                project.getTechStack(),
                project.getImageUrl(),
                project.getProjectUrl(),
                project.getGithubUrl(),
                project.getGroupName(),
                project.getFeatured(),
                project.getDisplayOrder()
        );
    }
}
