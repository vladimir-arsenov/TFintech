package org.example.service.snapshot;

import lombok.Getter;
import org.example.model.Category;

@Getter
public class CategorySnapshot {
    private final Integer id;
    private final String name;
    private final String slug;

    public CategorySnapshot(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
    }
}