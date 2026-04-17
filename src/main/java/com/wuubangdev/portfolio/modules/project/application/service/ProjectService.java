package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    List<ProjectResponse> getAll(String category, Boolean featured);
    PageResponse<ProjectResponse> getAllPaged(String category, Boolean featured, int page, int size);
    List<ProjectResponse> getFeatured();
    ProjectResponse getBySlug(String slug);
    ProjectResponse getById(Long id);
    List<ProjectResponse> getRelatedProjects(String slug, int limit);
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
}
