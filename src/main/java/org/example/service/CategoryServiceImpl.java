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
public class CategoryServiceImpl implements CategoryService {

    private final ConcurrentHashMapRepository<Integer, Category> repository;

    @Override
    public Category getCategory(Integer id) {
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }

    @Override
    public void addCategory(Category category) {
        repository.add(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return repository.getAll();
    }

    @Override
    public void updateCategory(Category newCategory) {
        Optional.ofNullable(repository.update(newCategory))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(newCategory.getId())));
    }

    @Override
    public void deleteCategory(Integer id) {
        Optional.ofNullable(repository.delete(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }
}
