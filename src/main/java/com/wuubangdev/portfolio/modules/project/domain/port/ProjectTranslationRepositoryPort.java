package com.wuubangdev.portfolio.modules.project.domain.port;

import com.wuubangdev.portfolio.modules.project.domain.model.ProjectTranslation;

import java.util.List;
import java.util.Optional;

public interface ProjectTranslationRepositoryPort {
    ProjectTranslation save(ProjectTranslation translation);
    List<ProjectTranslation> saveAll(List<ProjectTranslation> translations);
    Optional<ProjectTranslation> findByProjectIdAndLocale(Long projectId, String locale);
    List<ProjectTranslation> findByProjectIdInAndLocale(List<Long> projectIds, String locale);
    void deleteByProjectId(Long projectId);
}
