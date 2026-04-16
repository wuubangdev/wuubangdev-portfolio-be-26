package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.modules.post.application.dto.CategoryRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.CategoryResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Category;
import com.wuubangdev.portfolio.modules.post.domain.port.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepositoryPort.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepositoryPort.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepositoryPort.existsBySlug(request.slug())) {
            throw new RuntimeException("Slug already exists");
        }
        Category category = Category.builder()
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .build();
        return toResponse(categoryRepositoryPort.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setDescription(request.description());
        return toResponse(categoryRepositoryPort.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepositoryPort.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription()
        );
    }
}
