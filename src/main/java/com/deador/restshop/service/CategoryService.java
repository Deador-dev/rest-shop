package com.deador.restshop.service;

import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse addCategory(CategoryProfile categoryProfile);
}
