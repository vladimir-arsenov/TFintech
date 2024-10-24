package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.example.model.Category;
import org.example.service.hashMapService.HashMapCategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@LogExecutionTime
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/places/categories")
public class CategoryController {

    private final HashMapCategoryService hashMapCategoryService;

    @GetMapping
    public List<Category> getCategories() {
        return hashMapCategoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Integer id) {
        return hashMapCategoryService.getCategory(id);
    }

    @PostMapping
    public void addCategory(@RequestBody Category category) {
        hashMapCategoryService.addCategory(category);
    }

    @PutMapping("/{id}")
    public void updateCategory(@RequestBody Category category) {
        hashMapCategoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        hashMapCategoryService.deleteCategory(id);
    }
}

