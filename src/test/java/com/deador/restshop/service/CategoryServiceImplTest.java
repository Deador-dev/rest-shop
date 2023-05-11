package com.deador.restshop.service;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.BadRequestException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.exception.OperationWasCanceledException;
import com.deador.restshop.model.Category;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NOT_EXISTING_CATEGORY_ID = 100L;
    private static final String EXISTING_CATEGORY_NAME = "iPhone";
    private static final String NOT_EXISTING_CATEGORY_NAME = "Not existing category name";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String CATEGORY_ALREADY_EXIST_WITH_NAME = "Category already exist with name: %s";
    private static final String CATEGORY_DELETING_ERROR = "Can't delete category cause of relationships";
    private static final Boolean SHOULD_DELETE_ASSOCIATED_SMARTPHONES = true;
    private static final Boolean NOT_SHOULD_DELETE_ASSOCIATED_SMARTPHONES = false;
    private static final Long EXISTING_SMARTPHONE_ID = 1L;
    private static final String EXISTING_SMARTPHONE_NAME = "iPhone XS";
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SmartphoneRepository smartphoneRepository;
    @Mock
    private DTOConverter dtoConverter;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryProfile categoryProfile;
    private CategoryResponse categoryResponse;
    private List<Smartphone> smartphonesByCategoryId;

    @BeforeEach
    void init() {
        category = Category.builder().id(EXISTING_CATEGORY_ID).name(EXISTING_CATEGORY_NAME).build();
        categoryProfile = CategoryProfile.builder().name(EXISTING_CATEGORY_NAME).build();
        categoryResponse = CategoryResponse.builder().id(EXISTING_CATEGORY_ID).name(EXISTING_CATEGORY_NAME).build();
        smartphonesByCategoryId = Arrays.asList(
                Smartphone.builder().id(EXISTING_SMARTPHONE_ID).name(EXISTING_SMARTPHONE_NAME).build(),
                Smartphone.builder().id(EXISTING_SMARTPHONE_ID).name(EXISTING_SMARTPHONE_NAME).build()
        );
    }

    @Test
    void getListOfCategoryResponses_thenReturnListOfCategoryResponses() {
        List<Category> genericEntityList = Arrays.asList(
                Category.builder()
                        .id(NOT_EXISTING_CATEGORY_ID)
                        .name(NOT_EXISTING_CATEGORY_NAME)
                        .build(),
                Category.builder()
                        .id(NOT_EXISTING_CATEGORY_ID)
                        .name(NOT_EXISTING_CATEGORY_NAME)
                        .build()
        );
        CategoryResponse expectedCategoryResponse = CategoryResponse.builder().id(NOT_EXISTING_CATEGORY_ID).name(NOT_EXISTING_CATEGORY_NAME).build();

        when(categoryRepository.findAll()).thenReturn(genericEntityList);
        when(dtoConverter.convertToDTO(genericEntityList.get(0), CategoryResponse.class)).thenReturn(expectedCategoryResponse);

        List<CategoryResponse> mappedList = genericEntityList.stream()
                .map(genericEntity -> (CategoryResponse) dtoConverter.convertToDTO(genericEntity, CategoryResponse.class))
                .collect(Collectors.toList());
        List<CategoryResponse> categoryResponsesFromService = categoryService.getListOfCategoryResponses();

        assertEquals(mappedList, categoryResponsesFromService);
        assertEquals(expectedCategoryResponse, categoryResponsesFromService.get(0));
    }

    @Test
    void givenId_whenGetCategoryById_thenReturnCategory() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));

        assertThat(categoryService.getCategoryById(EXISTING_CATEGORY_ID)).isNotNull().isEqualTo(category);
    }

    @Test
    void givenNotExistingId_whenGetCategoryById_thenThrowNotExistException() {
        when(categoryRepository.findById(NOT_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(NOT_EXISTING_CATEGORY_ID))
                .isInstanceOf(NotExistException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID, NOT_EXISTING_CATEGORY_ID));
    }

    @Test
    void givenId_whenGetCategoryResponseById_thenReturnCategoryResponse() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(dtoConverter.convertToDTO(category, CategoryResponse.class)).thenReturn(categoryResponse);

        CategoryResponse categoryResponseFromService = categoryService.getCategoryResponseById(EXISTING_CATEGORY_ID);

        assertEquals(categoryResponse, categoryResponseFromService);
    }

    @Test
    void givenNotExistingId_whenGetCategoryResponseById_thenThrowNotExistException() {
        when(categoryRepository.findById(NOT_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryResponseById(NOT_EXISTING_CATEGORY_ID))
                .isInstanceOf(NotExistException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID, NOT_EXISTING_CATEGORY_ID));
    }

    @Test
    void givenCategoryProfile_whenAddCategory_thenReturnCategoryResponse() {
        Category mappedCategory = Category.builder().name(EXISTING_CATEGORY_NAME).build();
        when(dtoConverter.convertToEntity(categoryProfile, Category.class)).thenReturn(mappedCategory);
        when(dtoConverter.convertToDTO(category, CategoryResponse.class)).thenReturn(categoryResponse);
        when(categoryRepository.existsByName(categoryProfile.getName())).thenReturn(false);
        when(categoryRepository.save(mappedCategory)).thenReturn(category);

        assertEquals(categoryResponse, categoryService.addCategory(categoryProfile));
        verify(categoryRepository, times(1)).save(mappedCategory);
    }

    @Test
    void givenCategoryProfileWithExistingName_whenAddCategory_thenThrowAlreadyExistException() {
        when(categoryRepository.existsByName(categoryProfile.getName())).thenReturn(true);

        assertThatThrownBy(() -> categoryService.addCategory(categoryProfile))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(String.format(CATEGORY_ALREADY_EXIST_WITH_NAME, categoryProfile.getName()));
    }

    @Test
    void givenIdAndCategoryProfile_whenUpdateCategory_thenReturnCategoryResponse() {
        when(dtoConverter.convertToEntity(categoryProfile, Category.class)).thenReturn(category);
        when(dtoConverter.convertToDTO(category, CategoryResponse.class)).thenReturn(categoryResponse);
        when(categoryRepository.existsById(EXISTING_CATEGORY_ID)).thenReturn(true);
        when(categoryRepository.existsByName(EXISTING_CATEGORY_NAME)).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryResponse categoryResponseFromService = categoryService.updateCategory(EXISTING_CATEGORY_ID, categoryProfile);
        assertEquals(categoryResponse, categoryResponseFromService);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void givenNotExistingId_whenUpdateCategory_thenThrowNotExistException() {
        when(categoryRepository.existsById(NOT_EXISTING_CATEGORY_ID)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.updateCategory(NOT_EXISTING_CATEGORY_ID, categoryProfile))
                .isInstanceOf(NotExistException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID, NOT_EXISTING_CATEGORY_ID));
    }

    @Test
    void givenCategoryProfileWithAlreadyExistingName_whenUpdateCategory_thenThrowAlreadyExistException() {
        when(categoryRepository.existsById(EXISTING_CATEGORY_ID)).thenReturn(true);
        when(categoryRepository.existsByName(EXISTING_CATEGORY_NAME)).thenReturn(true);

        assertThatThrownBy(() -> categoryService.updateCategory(EXISTING_CATEGORY_ID, categoryProfile))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(String.format(CATEGORY_ALREADY_EXIST_WITH_NAME, EXISTING_CATEGORY_NAME));
    }

    @Test
    void givenIdWithEmptyListOfSmartphonesByCategoryId_whenDeleteCategoryById_thenReturnCategoryResponse() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(smartphoneRepository.findAllByCategoryId(EXISTING_CATEGORY_ID)).thenReturn(Collections.emptyList());
        when(dtoConverter.convertToDTO(category, CategoryResponse.class)).thenReturn(categoryResponse);

        assertEquals(categoryResponse, categoryService.deleteCategoryById(EXISTING_CATEGORY_ID, NOT_SHOULD_DELETE_ASSOCIATED_SMARTPHONES));
        verify(categoryRepository, times(1)).deleteById(EXISTING_CATEGORY_ID);
    }

    @Test
    void givenNotExistingId_whenDeleteCategoryById_thenThrowNotExistException() {
        when(categoryRepository.findById(NOT_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategoryById(NOT_EXISTING_CATEGORY_ID, NOT_SHOULD_DELETE_ASSOCIATED_SMARTPHONES))
                .isInstanceOf(NotExistException.class)
                .hasMessage(String.format(CATEGORY_NOT_FOUND_BY_ID, NOT_EXISTING_CATEGORY_ID));
    }

    @Test
    void givenIdWithListOfSmartphonesByCategoryIdAndShouldDeleteAssociatedSmartphones_whenDeleteCategoryById_thenReturnCategoryResponse() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(smartphoneRepository.findAllByCategoryId(EXISTING_CATEGORY_ID)).thenReturn(smartphonesByCategoryId);
        when(dtoConverter.convertToDTO(category, CategoryResponse.class)).thenReturn(categoryResponse);

        assertEquals(categoryResponse, categoryService.deleteCategoryById(EXISTING_CATEGORY_ID, SHOULD_DELETE_ASSOCIATED_SMARTPHONES));
        verify(categoryRepository, times(1)).deleteById(EXISTING_CATEGORY_ID);
    }

    @Test
    void givenIdWithListOfSmartphonesByCategoryIdAndNotShouldDeleteAssociatedSmartphones_whenDeleteCategoryById_thenThrowOperationWasCanceledException() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(smartphoneRepository.findAllByCategoryId(EXISTING_CATEGORY_ID)).thenReturn(smartphonesByCategoryId);

        assertThatThrownBy(() -> categoryService.deleteCategoryById(EXISTING_CATEGORY_ID, NOT_SHOULD_DELETE_ASSOCIATED_SMARTPHONES))
                .isInstanceOf(OperationWasCanceledException.class);
    }

    @Test
    void givenIdWithListOfSmartphonesByCategoryIdAndNullShouldDeleteAssociatedSmartphones_whenDeleteCategoryById_thenThrowBadRequestException() {
        when(categoryRepository.findById(EXISTING_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(smartphoneRepository.findAllByCategoryId(EXISTING_CATEGORY_ID)).thenReturn(smartphonesByCategoryId);

        assertThatThrownBy(() -> categoryService.deleteCategoryById(EXISTING_CATEGORY_ID, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(CATEGORY_DELETING_ERROR);
    }
}
