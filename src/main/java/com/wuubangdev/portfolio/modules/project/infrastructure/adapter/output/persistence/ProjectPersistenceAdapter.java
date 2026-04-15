package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.project.domain.model.Project;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
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
                .displayOrder(e.getDisplayOrder()).build();
    }

    private ProjectJpaEntity toEntity(Project p) {
        ProjectJpaEntity e = new ProjectJpaEntity();
        e.setId(p.getId()); e.setTitle(p.getTitle()); e.setSlug(p.getSlug()); e.setCategory(p.getCategory()); e.setTags(p.getTags()); e.setDescription(p.getDescription()); e.setContent(p.getContent());
        e.setTechStack(p.getTechStack()); e.setImageUrl(p.getImageUrl()); e.setProjectUrl(p.getProjectUrl());
        e.setGithubUrl(p.getGithubUrl()); e.setGroupName(p.getGroupName()); e.setFeatured(p.getFeatured());
        e.setDisplayOrder(p.getDisplayOrder());
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
    @Override public List<Project> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Project> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public Optional<Project> findBySlug(String slug) { return repo.findBySlug(slug).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
    @Override public boolean existsBySlug(String slug) { return repo.existsBySlug(slug); }
}
