package org.example.repository;

import org.example.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CategoryRepository implements ConcurrentHashMapRepository<Integer, Category> {

    private final ConcurrentHashMap<Integer, Category> storage;

    public CategoryRepository() {
        storage = new ConcurrentHashMap<>();
    }

    @Override
    public Category get(Integer id) {
        return storage.get(id);
    }

    @Override
    public void add(Category e) {
        storage.put(e.getId(), e);
    }

    @Override
    public List<Category> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Category update(Category e) {
        if (!storage.containsKey(e.getId()))
            return null;

        return storage.put(e.getId(), e);
    }

    @Override
    public Category delete(Integer id) {
        System.out.println(id);
        return storage.remove(id);
    }
}
