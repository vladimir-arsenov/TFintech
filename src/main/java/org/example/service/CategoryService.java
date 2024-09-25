package org.example.service;

import org.example.model.Category;

import java.util.List;

public interface CategoryService {

    Category getCategory(Integer id);

    void addCategory(Category category);

    List<Category> getAllCategories();

    void updateCategory(Category newCategory);

    void deleteCategory(Integer id);
}
