package com.deador.restshop.service;

import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryResponseById(Long id);

    CategoryResponse addCategory(CategoryProfile categoryProfile);

    CategoryResponse updateCategory(Long id, CategoryProfile categoryProfile);

    CategoryResponse deleteCategoryById(Long id);
}
