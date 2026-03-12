package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    List<ProjectResponse> getAll();
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
}
