package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.project.application.mapper.ProjectApplicationMapper;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectApplicationMapper projectApplicationMapper;

    @Override @Transactional
    public ProjectResponse create(ProjectRequest request) {
        if (projectRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Project project = projectApplicationMapper.toNewDomain(request);
        return projectApplicationMapper.toResponse(projectRepositoryPort.save(project));
    }

    @Override
    public List<ProjectResponse> getAll(String category, Boolean featured) {
        List<Project> projects;
        if (Boolean.TRUE.equals(featured)) {
            projects = projectRepositoryPort.findFeatured();
        } else if (category != null && !category.isBlank()) {
            projects = projectRepositoryPort.findAllByCategory(category.trim());
        } else {
            projects = projectRepositoryPort.findAll();
        }
        return projects.stream().map(projectApplicationMapper::toResponse).toList();
    }

    @Override
    public PageResponse<ProjectResponse> getAllPaged(String category, Boolean featured, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.desc("id")));
        List<Project> projects;
        long total;
        if (Boolean.TRUE.equals(featured)) {
            projects = projectRepositoryPort.findFeaturedPaged(pageable);
            total = projectRepositoryPort.countFeatured();
        } else if (category != null && !category.isBlank()) {
            projects = projectRepositoryPort.findByCategoryPaged(category.trim(), pageable);
            total = projectRepositoryPort.countByCategory(category.trim());
        } else {
            projects = projectRepositoryPort.findAllPaged(pageable);
            total = projectRepositoryPort.countAll();
        }
        return PageResponse.of(projects.stream().map(projectApplicationMapper::toResponse).toList(), page, size, total);
    }

    @Override
    public List<ProjectResponse> getFeatured() {
        return projectRepositoryPort.findFeatured().stream().map(projectApplicationMapper::toResponse).toList();
    }

    @Override
    public ProjectResponse getBySlug(String slug) {
        return projectApplicationMapper.toResponse(projectRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project with slug: " + slug)));
    }

    @Override
    public ProjectResponse getById(Long id) {
        return projectApplicationMapper.toResponse(projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id)));
    }

    @Override
    public List<ProjectResponse> getRelatedProjects(String slug, int limit) {
        Project project = projectRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project with slug: " + slug));
        if (project.getCategory() == null || project.getCategory().isBlank()) {
            return List.of();
        }
        return projectRepositoryPort.findRelatedProjects(project.getCategory(), project.getId(), limit)
                .stream()
                .map(projectApplicationMapper::toResponse)
                .toList();
    }

    @Override @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project p = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        if (!p.getSlug().equals(request.slug()) && projectRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Project updatedProject = projectApplicationMapper.updateDomain(p, request);
        return projectApplicationMapper.toResponse(projectRepositoryPort.save(updatedProject));
    }

    @Override @Transactional
    public void delete(Long id) {
        projectRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
        projectRepositoryPort.deleteById(id);
    }
}
