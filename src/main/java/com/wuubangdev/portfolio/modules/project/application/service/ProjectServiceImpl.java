package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
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

    @Override @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Project project = Project.builder()
                .title(request.title()).description(request.description()).techStack(request.techStack())
                .imageUrl(request.imageUrl()).projectUrl(request.projectUrl()).githubUrl(request.githubUrl())
                .groupName(request.groupName()).featured(request.featured() != null ? request.featured() : false)
                .displayOrder(request.displayOrder()).build();
        return toResponse(projectRepositoryPort.save(project));
    }

    @Override
    public List<ProjectResponse> getAll() {
        return projectRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project p = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        p.setTitle(request.title()); p.setDescription(request.description());
        p.setTechStack(request.techStack()); p.setImageUrl(request.imageUrl());
        p.setProjectUrl(request.projectUrl()); p.setGithubUrl(request.githubUrl());
        p.setGroupName(request.groupName()); p.setFeatured(request.featured()); p.setDisplayOrder(request.displayOrder());
        return toResponse(projectRepositoryPort.save(p));
    }

    @Override @Transactional
    public void delete(Long id) {
        projectRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
        projectRepositoryPort.deleteById(id);
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(p.getId(), p.getTitle(), p.getDescription(), p.getTechStack(),
                p.getImageUrl(), p.getProjectUrl(), p.getGithubUrl(), p.getGroupName(), p.getFeatured(), p.getDisplayOrder());
    }
}
