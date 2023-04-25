package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.exception.*;
import com.deador.restshop.model.Category;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_ALREADY_EXIST_WITH_NAME = "Category already exist with name: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationships";
    private final CategoryRepository categoryRepository;
    private final SmartphoneRepository smartphoneRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               SmartphoneRepository smartphoneRepository,
                               DTOConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.smartphoneRepository = smartphoneRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<CategoryResponse> getListOfCategoryResponses() {
        List<CategoryResponse> allCategoryResponses = categoryRepository.findAll().stream()
                .map(category -> (CategoryResponse) dtoConverter.convertToDTO(category, CategoryResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of category responses '{}'", allCategoryResponses);
        return allCategoryResponses;
    }

    public Category getCategoryById(Long id) {
        log.debug("get category by id '{}'", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("category not found by id '{}'", id);
                    return new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public CategoryResponse getCategoryResponseById(Long id) {
        Category category = getCategoryById(id);

        log.debug("get category response by id '{}'", id);
        return dtoConverter.convertToDTO(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse addCategory(CategoryProfile categoryProfile) {
        if (categoryRepository.existsByName(categoryProfile.getName())) {
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST_WITH_NAME, categoryProfile.getName()));
        }

        Category category = categoryRepository.save(dtoConverter.convertToEntity(categoryProfile, Category.class));

        log.debug("category was created successfully '{}'", category);
        return dtoConverter.convertToDTO(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryProfile categoryProfile) {
        if (!categoryRepository.existsById(id)) {
            log.error("category not found by id '{}'", id);
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        } else if (categoryRepository.existsByName(categoryProfile.getName())) {
            log.error("category already exist with name '{}'", categoryProfile.getName());
            throw new AlreadyExistException(String.format(CATEGORY_ALREADY_EXIST_WITH_NAME, categoryProfile.getName()));
        }

        Category category = dtoConverter.convertToEntity(categoryProfile, Category.class);
        category.setId(id);

        log.debug("category with id '{}' was updated successfully '{}'", id, category);
        return dtoConverter.convertToDTO(categoryRepository.save(category), CategoryResponse.class);
    }

    @Override
    public CategoryResponse deleteCategoryById(Long id, Boolean shouldDeleteAssociatedSmartphones) {
        Category category = getCategoryById(id);
        List<Smartphone> smartphonesByCategoryId = smartphoneRepository.findAllByCategoryId(id);

        try {
            if (smartphonesByCategoryId.isEmpty()) {
                categoryRepository.deleteById(id);
                categoryRepository.flush();
                log.debug("category was successfully deleted by id '{}'", id);
                return dtoConverter.convertToDTO(category, CategoryResponse.class);
            }

            if (shouldDeleteAssociatedSmartphones != null && shouldDeleteAssociatedSmartphones) {
                smartphonesByCategoryId.forEach(smartphone -> smartphoneRepository.deleteById(smartphone.getId()));
                categoryRepository.deleteById(id);
                categoryRepository.flush();

                log.debug("category was successfully deleted by id '{}'", id);
                return dtoConverter.convertToDTO(category, CategoryResponse.class);
            } else if (shouldDeleteAssociatedSmartphones != null) {
                throw new OperationWasCanceledException();
            }

            throw new BadRequestException(CATEGORY_DELETING_ERROR);
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(CATEGORY_DELETING_ERROR);
        }
    }
}