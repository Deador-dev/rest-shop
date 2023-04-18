package com.deador.restshop.controller;

import com.deador.restshop.dto.category.CategoryProfile;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategoryResponses());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryResponseById(id));
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryProfile categoryProfile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(categoryProfile));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody CategoryProfile categoryProfile) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(id, categoryProfile));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id,
                                                           @RequestParam(required = false) Boolean shouldDeleteAssociatedSmartphones) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategoryById(id, shouldDeleteAssociatedSmartphones));
    }
}
