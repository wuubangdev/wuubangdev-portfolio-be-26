package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.modules.post.application.dto.CategoryRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.CategoryResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
