package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.project.application.mapper.ProjectApplicationMapper;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
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
    public List<ProjectResponse> getAll() {
        return projectRepositoryPort.findAll().stream().map(projectApplicationMapper::toResponse).toList();
    }

    @Override
    public ProjectResponse getBySlug(String slug) {
        return projectApplicationMapper.toResponse(projectRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project with slug: " + slug)));
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
