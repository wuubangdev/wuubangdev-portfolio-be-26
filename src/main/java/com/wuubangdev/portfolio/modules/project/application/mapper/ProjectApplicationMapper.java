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
                .titleSeo(defaultIfBlank(request.titleSeo(), request.title()))
                .descriptionSeo(defaultIfBlank(request.descriptionSeo(), request.description()))
                .thumbnailSeo(defaultIfBlank(request.thumbnailSeo(), request.imageUrl()))
                .seoKeywords(request.seoKeywords())
                .canonicalUrl(request.canonicalUrl())
                .indexable(request.indexable() == null ? Boolean.TRUE : request.indexable())
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
        project.setTitleSeo(defaultIfBlank(request.titleSeo(), request.title()));
        project.setDescriptionSeo(defaultIfBlank(request.descriptionSeo(), request.description()));
        project.setThumbnailSeo(defaultIfBlank(request.thumbnailSeo(), request.imageUrl()));
        project.setSeoKeywords(request.seoKeywords());
        project.setCanonicalUrl(request.canonicalUrl());
        project.setIndexable(request.indexable() == null ? Boolean.TRUE : request.indexable());
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
                project.getDisplayOrder(),
                project.getTitleSeo(),
                project.getDescriptionSeo(),
                project.getThumbnailSeo(),
                project.getSeoKeywords(),
                project.getCanonicalUrl(),
                project.getIndexable(),
                project.getCreatedAt()
        );
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
