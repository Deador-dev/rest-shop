package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.entity.Category;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_ALREADY_EXIST_WITH_NAME = "Category already exist with name: %s";
    private final CategoryRepository categoryRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               DTOConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> allCategoryResponses = categoryRepository.findAll().stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDTO(category, CategoryResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of categories = " + allCategoryResponses);
        return allCategoryResponses;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id)));

        log.debug("**/getting category by id = " + category);

        return dtoConverter.convertToDTO(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse addCategory(CategoryProfile categoryProfile) {
        if (categoryRepository.existsByName(categoryProfile.getName())) {
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST_WITH_NAME, categoryProfile.getName()));
        }

        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile, Category.class));
        log.debug("**/adding new category = " + category);

        return dtoConverter.convertToDTO(category, CategoryResponse.class);
    }
}
