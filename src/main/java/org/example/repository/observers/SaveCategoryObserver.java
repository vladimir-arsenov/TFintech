package org.example.repository.observers;

import lombok.RequiredArgsConstructor;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveCategoryObserver implements Observer<Category> {
    private final CategoryRepository repository;

    @Override
    public void update(Category entity) {
        repository.save(entity);
    }
}
