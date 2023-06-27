package com.deador.restshop.controller;

import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.dockerjava.api.exception.NotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryControllerTest {
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NOT_EXISTING_CATEGORY_ID = 100L;
    private static final String NOT_NUMBER_CATEGORY_ID = "not_number";
    private static final String EXISTING_CATEGORY_NAME = "iPhone";
    private static final String NOT_EXISTING_CATEGORY_NAME = "Xiaomi";
    private MockMvc mockMvc;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private CategoryController categoryController;
    private final Principal principal = () -> "test@gmail.com";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @SneakyThrows
    void getCategoriesTest() {
        List<CategoryResponse> expectedCategoryResponses = List.of(
                CategoryResponse.builder().id(EXISTING_CATEGORY_ID).name("test_1").build(),
                CategoryResponse.builder().id(EXISTING_CATEGORY_ID).name("test_2").build()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectWriter ow = objectMapper.writer();
        String expectedJson = ow.writeValueAsString(expectedCategoryResponses);

        when(categoryService.getListOfCategoryResponses()).thenReturn(expectedCategoryResponses);

        mockMvc.perform(get("/categories")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(categoryService, times(1)).getListOfCategoryResponses();
    }

    @Test
    @SneakyThrows
    void getCategoryTest() {
        mockMvc.perform(get("/category/{id}", EXISTING_CATEGORY_ID)
                        .principal(principal))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getCategoryResponseById(EXISTING_CATEGORY_ID);
    }

    @Test
    @SneakyThrows
    void getCategoryNotFoundTest() {
        doThrow(new NotFoundException("Error message"))
                .when(categoryService)
                .getCategoryResponseById(NOT_EXISTING_CATEGORY_ID);

        assertThatThrownBy(() -> mockMvc.perform(get("/category/{id}", NOT_EXISTING_CATEGORY_ID).principal(principal))
                .andExpect(status().isNotFound()))
                .hasCause(new NotFoundException("Error message"));
    }

    @Test
    @SneakyThrows
    void getCategoryBadRequestTest() {
        mockMvc.perform(get("/category/{id}", NOT_NUMBER_CATEGORY_ID).principal(principal))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(0)).getCategoryResponseById(EXISTING_CATEGORY_ID);
    }

    @Test
    @SneakyThrows
    void getCategoryResponseTest() {
        CategoryResponse categoryResponse = CategoryResponse.builder().id(EXISTING_CATEGORY_ID).name(EXISTING_CATEGORY_NAME).build();

        when(categoryService.getCategoryResponseById(EXISTING_CATEGORY_ID)).thenReturn(categoryResponse);

        MvcResult result = mockMvc.perform(get("/category/{id}", EXISTING_CATEGORY_ID)
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        CategoryResponse categoryResponseFromJson = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponse.class);

        assertThat(categoryResponse).isEqualTo(categoryResponseFromJson);
        verify(categoryService, times(1)).getCategoryResponseById(EXISTING_CATEGORY_ID);
    }

    @Test
    @SneakyThrows
    void addCategoryTest() {
        CategoryProfile categoryProfile = CategoryProfile.builder().name(NOT_EXISTING_CATEGORY_NAME).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(categoryProfile);

        mockMvc.perform(post("/category")
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(categoryService, times(1)).addCategory(categoryProfile);
    }

    @Test
    @SneakyThrows
    void addCategoryBadRequestTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = "{}";

        mockMvc.perform(post("/category")
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void addCategoryConflictTest() {
        CategoryProfile categoryProfile = CategoryProfile.builder().name(EXISTING_CATEGORY_NAME).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(categoryProfile);

        doThrow(new AlreadyExistException("Error message"))
                .when(categoryService)
                .addCategory(categoryProfile);

        assertThatThrownBy(() -> mockMvc.perform(post("/category")
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()))
                .hasCause(new AlreadyExistException("Error message"));
    }


    @Test
    @SneakyThrows
    void updateCategoryTest() {
        CategoryProfile categoryProfile = CategoryProfile.builder().name(NOT_EXISTING_CATEGORY_NAME).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(categoryProfile);

        mockMvc.perform(put("/category/{id}", EXISTING_CATEGORY_ID)
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).updateCategory(EXISTING_CATEGORY_ID, categoryProfile);
    }

    @Test
    @SneakyThrows
    void updateCategoryBadRequestTest() {
        CategoryProfile categoryProfile = CategoryProfile.builder().name(NOT_EXISTING_CATEGORY_NAME).build();

        doThrow(new NotFoundException("Error message"))
                .when(categoryService)
                .updateCategory(NOT_EXISTING_CATEGORY_ID, categoryProfile);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(categoryProfile);

        assertThatThrownBy(() -> mockMvc.perform(put("/category/{id}", NOT_EXISTING_CATEGORY_ID)
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()))
                .hasCause(new NotFoundException("Error message"));
    }

    @Test
    @SneakyThrows
    void updateCategoryConflictTest() {
        CategoryProfile categoryProfile = CategoryProfile.builder().name(EXISTING_CATEGORY_NAME).build();

        doThrow(new AlreadyExistException("Error message"))
                .when(categoryService)
                .updateCategory(EXISTING_CATEGORY_ID, categoryProfile);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String json = objectMapper.writeValueAsString(categoryProfile);

        assertThatThrownBy(() -> mockMvc.perform(put("/category/{id}", EXISTING_CATEGORY_ID)
                        .principal(principal)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()))
                .hasCause(new AlreadyExistException("Error message"));
    }

    @Test
    @SneakyThrows
    void deleteCategoryTest() {
        mockMvc.perform(delete("/category/{id}", EXISTING_CATEGORY_ID)
                        .principal(principal)
                        .param("shouldDeleteAssociatedSmartphones", "true"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategoryById(EXISTING_CATEGORY_ID, Boolean.TRUE);
    }
}