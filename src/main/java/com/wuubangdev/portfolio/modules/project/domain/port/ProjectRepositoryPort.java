package com.wuubangdev.portfolio.modules.project.domain.port;

import com.wuubangdev.portfolio.modules.project.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {
    Project save(Project project);
    List<Project> findAll();
    Optional<Project> findById(Long id);
    Optional<Project> findBySlug(String slug);
    void deleteById(Long id);
    boolean existsBySlug(String slug);
}
