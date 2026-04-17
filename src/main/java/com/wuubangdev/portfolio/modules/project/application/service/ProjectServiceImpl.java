package com.wuubangdev.portfolio.modules.project.application.service;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.infrastructure.global.exception.BusinessException;
import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.project.application.mapper.ProjectApplicationMapper;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectRequest;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectResponse;
import com.wuubangdev.portfolio.modules.project.application.dto.ProjectTranslationRequest;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import com.wuubangdev.portfolio.modules.project.domain.model.ProjectTranslation;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectTranslationRepositoryPort projectTranslationRepositoryPort;
    private final ProjectApplicationMapper projectApplicationMapper;

    @Override @Transactional
    public ProjectResponse create(ProjectRequest request) {
        if (projectRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Project project = projectApplicationMapper.toNewDomain(request);
        Project savedProject = projectRepositoryPort.save(project);
        syncTranslations(savedProject.getId(), request);
        return toLocalizedResponse(savedProject, resolveLocaleCode());
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
        return toLocalizedResponses(projects, resolveLocaleCode());
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
        return PageResponse.of(toLocalizedResponses(projects, resolveLocaleCode()), page, size, total);
    }

    @Override
    public List<ProjectResponse> getFeatured() {
        return toLocalizedResponses(projectRepositoryPort.findFeatured(), resolveLocaleCode());
    }

    @Override
    public ProjectResponse getBySlug(String slug) {
        Project project = projectRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project with slug: " + slug));
        return toLocalizedResponse(project, resolveLocaleCode());
    }

    @Override
    public ProjectResponse getById(Long id) {
        Project project = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        return toLocalizedResponse(project, resolveLocaleCode());
    }

    @Override
    public List<ProjectResponse> getRelatedProjects(String slug, int limit) {
        Project project = projectRepositoryPort.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project with slug: " + slug));
        if (project.getCategory() == null || project.getCategory().isBlank()) {
            return List.of();
        }
        return toLocalizedResponses(projectRepositoryPort.findRelatedProjects(project.getCategory(), project.getId(), limit), resolveLocaleCode());
    }

    @Override @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project p = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        if (!p.getSlug().equals(request.slug()) && projectRepositoryPort.existsBySlug(request.slug())) {
            throw new BusinessException("Slug '" + request.slug() + "' already exists");
        }
        Project updatedProject = projectApplicationMapper.updateDomain(p, request);
        Project savedProject = projectRepositoryPort.save(updatedProject);
        syncTranslations(savedProject.getId(), request);
        return toLocalizedResponse(savedProject, resolveLocaleCode());
    }

    @Override
    @Transactional
    public ProjectResponse upsertTranslation(Long id, String locale, ProjectTranslationRequest request) {
        Project project = projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        projectTranslationRepositoryPort.save(ProjectTranslation.builder()
                .projectId(id)
                .locale(normalizeLocale(locale))
                .title(request.title())
                .description(request.description())
                .content(request.content())
                .titleSeo(request.titleSeo())
                .descriptionSeo(request.descriptionSeo())
                .seoKeywords(request.seoKeywords())
                .build());
        return toLocalizedResponse(project, normalizeLocale(locale));
    }

    @Override @Transactional
    public void delete(Long id) {
        projectRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", id));
        projectRepositoryPort.deleteById(id);
        projectTranslationRepositoryPort.deleteByProjectId(id);
    }

    private void syncTranslations(Long projectId, ProjectRequest request) {
        if (request.translations() == null || request.translations().isEmpty()) {
            return;
        }

        List<ProjectTranslation> translations = request.translations().stream()
                .map(translation -> ProjectTranslation.builder()
                        .projectId(projectId)
                        .locale(normalizeLocale(translation.locale()))
                        .title(translation.title())
                        .description(translation.description())
                        .content(translation.content())
                        .titleSeo(translation.titleSeo())
                        .descriptionSeo(translation.descriptionSeo())
                        .seoKeywords(translation.seoKeywords())
                        .build())
                .toList();

        projectTranslationRepositoryPort.saveAll(translations);
    }

    private List<ProjectResponse> toLocalizedResponses(List<Project> projects, String locale) {
        if (projects.isEmpty()) {
            return List.of();
        }

        List<Long> projectIds = projects.stream().map(Project::getId).toList();
        Map<Long, ProjectTranslation> translationMap = new HashMap<>();
        projectTranslationRepositoryPort.findByProjectIdInAndLocale(projectIds, locale)
                .forEach(translation -> translationMap.put(translation.getProjectId(), translation));

        return projects.stream()
                .map(project -> projectApplicationMapper.toResponse(
                        projectApplicationMapper.applyTranslation(project, translationMap.get(project.getId())),
                        locale,
                        translationMap.get(project.getId()) != null))
                .toList();
    }

    private ProjectResponse toLocalizedResponse(Project project, String locale) {
        ProjectTranslation translation = projectTranslationRepositoryPort.findByProjectIdAndLocale(project.getId(), locale).orElse(null);
        return projectApplicationMapper.toResponse(projectApplicationMapper.applyTranslation(project, translation), locale, translation != null);
    }

    private String resolveLocaleCode() {
        return normalizeLocale(LocaleContextHolder.getLocale().getLanguage());
    }

    private String normalizeLocale(String locale) {
        if (locale == null || locale.isBlank()) {
            return "vi";
        }
        return locale.toLowerCase(Locale.ROOT);
    }
}
