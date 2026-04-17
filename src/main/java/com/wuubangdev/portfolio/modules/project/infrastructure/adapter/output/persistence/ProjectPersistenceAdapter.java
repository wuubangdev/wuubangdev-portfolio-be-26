package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private static final String SEQUENCE_NAME = "projects_sequence";

    private final ProjectJpaRepository repo;
    private final MongoSequenceService sequenceService;

    private Project toDomain(ProjectJpaEntity e) {
        return Project.builder().id(e.getId()).title(e.getTitle()).slug(e.getSlug()).category(e.getCategory()).tags(e.getTags()).description(e.getDescription()).content(e.getContent())
                .techStack(e.getTechStack()).imageUrl(e.getImageUrl()).projectUrl(e.getProjectUrl())
                .githubUrl(e.getGithubUrl()).groupName(e.getGroupName()).featured(e.getFeatured())
                .displayOrder(e.getDisplayOrder()).titleSeo(e.getTitleSeo()).descriptionSeo(e.getDescriptionSeo())
                .thumbnailSeo(e.getThumbnailSeo()).seoKeywords(e.getSeoKeywords()).canonicalUrl(e.getCanonicalUrl())
                .indexable(e.getIndexable()).createdAt(e.getCreatedAt()).build();
    }

    private ProjectJpaEntity toEntity(Project p) {
        ProjectJpaEntity e = new ProjectJpaEntity();
        e.setId(p.getId()); e.setTitle(p.getTitle()); e.setSlug(p.getSlug()); e.setCategory(p.getCategory()); e.setTags(p.getTags()); e.setDescription(p.getDescription()); e.setContent(p.getContent());
        e.setTechStack(p.getTechStack()); e.setImageUrl(p.getImageUrl()); e.setProjectUrl(p.getProjectUrl());
        e.setGithubUrl(p.getGithubUrl()); e.setGroupName(p.getGroupName()); e.setFeatured(p.getFeatured());
        e.setDisplayOrder(p.getDisplayOrder());
        e.setTitleSeo(p.getTitleSeo());
        e.setDescriptionSeo(p.getDescriptionSeo());
        e.setThumbnailSeo(p.getThumbnailSeo());
        e.setSeoKeywords(p.getSeoKeywords());
        e.setCanonicalUrl(p.getCanonicalUrl());
        e.setIndexable(p.getIndexable());
        return e;
    }

    @Override
    public Project save(Project p) {
        ProjectJpaEntity entity = toEntity(p);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repo.save(entity));
    }
    @Override public List<Project> findAll() { return repo.findAllByOrderByDisplayOrderAscIdDesc().stream().map(this::toDomain).toList(); }
    @Override public List<Project> findAllByCategory(String category) { return repo.findByCategoryIgnoreCaseOrderByDisplayOrderAscIdDesc(category).stream().map(this::toDomain).toList(); }
    @Override public List<Project> findFeatured() { return repo.findByFeaturedTrueOrderByDisplayOrderAscIdDesc().stream().map(this::toDomain).toList(); }
    @Override public List<Project> findAllPaged(Pageable pageable) { return repo.findAllBy(pageable).stream().map(this::toDomain).toList(); }
    @Override public List<Project> findByCategoryPaged(String category, Pageable pageable) { return repo.findByCategoryIgnoreCase(category, pageable).stream().map(this::toDomain).toList(); }
    @Override public List<Project> findFeaturedPaged(Pageable pageable) { return repo.findByFeaturedTrue(pageable).stream().map(this::toDomain).toList(); }
    @Override public long countAll() { return repo.count(); }
    @Override public long countByCategory(String category) { return repo.countByCategoryIgnoreCase(category); }
    @Override public long countFeatured() { return repo.countByFeaturedTrue(); }
    @Override public List<Project> findRelatedProjects(String category, Long excludeId, int limit) { return repo.findByCategoryIgnoreCaseAndIdNot(category, excludeId, org.springframework.data.domain.PageRequest.of(0, limit)).stream().map(this::toDomain).toList(); }
    @Override public Optional<Project> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public Optional<Project> findBySlug(String slug) { return repo.findBySlug(slug).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
    @Override public boolean existsBySlug(String slug) { return repo.existsBySlug(slug); }
}
