package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Category;
import org.example.repository.ConcurrentHashMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final ConcurrentHashMapRepository<Integer, Category> repository;

    public Category getCategory(Integer id) {
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }

    public void addCategory(Category category) {
        repository.add(category);
    }

    public List<Category> getAllCategories() {
        return repository.getAll();
    }

    public void updateCategory(Category newCategory) {
        Optional.ofNullable(repository.update(newCategory))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(newCategory.getId())));
    }

    public void deleteCategory(Integer id) {
        Optional.ofNullable(repository.delete(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }
}
