package com.wuubangdev.portfolio.modules.post.domain.port;

import com.wuubangdev.portfolio.modules.post.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findBySlug(String slug);
    void deleteById(Long id);
    boolean existsBySlug(String slug);
}
