package org.example.repository;

import org.example.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryRepositoryTests {

    private CategoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CategoryRepository();
    }

    @Test
    public void get_validId_shouldReturnCategory() {
        var category = new Category(1, "", "");
        repository.add(category);

        var result = repository.get(category.getId());

        assertEquals(category, result);
    }

    @Test
    public void get_invalidId_shouldReturnNull() {
        var category = new Category(1, "", "");
        repository.add(category);

        var result = repository.get(category.getId() + 1);

        assertNull(result);
    }

    @Test
    public void get_nullId_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.get(null));
    }

    @Test
    public void getAll_shouldReturnAllCategories() {
        var categories = List.of(new Category(1, "", ""),
                new Category(2, "", ""), new Category(3, "", ""));
        categories.forEach(repository::add);

        var result = repository.getAll();

        assertEquals(categories, result);
    }

    @Test
    public void add_existingCategory_shouldReplaceCategory() {
        var category = new Category(1, "", "");
        var newCategory = new Category(category.getId(), "", "new");
        repository.add(category);

        repository.add(newCategory);

        assertEquals(newCategory, repository.get(category.getId()));
    }

    @Test
    public void add_newCategory_shouldAdd() {
        var category = new Category(1, "", "");

        repository.add(category);

        assertEquals(category, repository.get(category.getId()));
    }

    @Test
    public void add_nullCategory_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.add(null));
    }

    @Test
    public void update_existingCategory_shouldUpdate() {
        var category = new Category(1, "", "");
        var updatedCategory = new Category(category.getId(), "", "updated");
        repository.add(category);

        repository.update(updatedCategory);
        var result = repository.get(category.getId());

        assertEquals(updatedCategory, result);
    }

    @Test
    public void update_nonExistingCategory_shouldReturnNull() {
        assertNull(repository.update(new Category(1, "", "")));
    }

    @Test
    public void update_nullCategory_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.update(null));
    }

    @Test
    public void delete_existingCategory_shouldRemove() {
        var category = new Category(1, "", "");
        repository.add(category);

        var result = repository.delete(category.getId());

        assertEquals(category, result);
        assertNull(repository.get(category.getId()));
    }

    @Test
    public void delete_nonExistingCategory_shouldReturnNull() {
        assertNull(repository.delete(1));
    }

    @Test
    public void delete_nullCategory_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.delete(null));
    }
}
