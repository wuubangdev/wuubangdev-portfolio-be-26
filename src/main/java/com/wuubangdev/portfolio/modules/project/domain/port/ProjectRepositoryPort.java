package com.wuubangdev.portfolio.modules.project.domain.port;

import org.springframework.data.domain.Pageable;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {
    Project save(Project project);
    List<Project> findAll();
    List<Project> findAllByCategory(String category);
    List<Project> findFeatured();
    List<Project> findAllPaged(Pageable pageable);
    List<Project> findByCategoryPaged(String category, Pageable pageable);
    List<Project> findFeaturedPaged(Pageable pageable);
    long countAll();
    long countByCategory(String category);
    long countFeatured();
    List<Project> findRelatedProjects(String category, Long excludeId, int limit);
    Optional<Project> findById(Long id);
    Optional<Project> findBySlug(String slug);
    void deleteById(Long id);
    boolean existsBySlug(String slug);
}
