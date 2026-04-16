package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.post.domain.model.Category;
import com.wuubangdev.portfolio.modules.post.domain.port.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private static final String SEQUENCE_NAME = "categories_sequence";

    private final CategoryJpaRepository repo;
    private final CategoryMapper mapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = mapper.toEntity(category);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return repo.findBySlug(slug).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repo.existsBySlug(slug);
    }
}
