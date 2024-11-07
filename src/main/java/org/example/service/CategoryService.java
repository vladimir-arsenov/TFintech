package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Category;
import org.example.repository.ConcurrentHashMapRepository;
import org.example.repository.observers.SaveCategoryObserver;
import org.example.service.snapshot.CategorySnapshot;
import org.example.service.snapshot.SnapshotManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final ConcurrentHashMapRepository<Integer, Category> repository;
    private final SaveCategoryObserver saveCategoryObserver;
    private final SnapshotManager<CategorySnapshot> snapshotManager;

    public Category getCategory(Integer id) {
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }

    public void addCategory(Category category) {
        snapshotManager.saveSnapshot("add", new CategorySnapshot(category));
        saveCategoryObserver.update(category);
    }

    public List<Category> getAllCategories() {
        return repository.getAll();
    }

    public void updateCategory(Category newCategory) {
        snapshotManager.saveSnapshot("update", new CategorySnapshot(newCategory));
        Optional.ofNullable(repository.update(newCategory))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(newCategory.getId())));
    }

    public void deleteCategory(Integer id) {
        snapshotManager.saveSnapshot("delete", new CategorySnapshot(getCategory(id)));
        Optional.ofNullable(repository.delete(id))
                .orElseThrow(() -> new NoSuchElementException("Category with id %d not found".formatted(id)));
    }
}
