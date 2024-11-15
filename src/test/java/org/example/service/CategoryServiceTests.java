package org.example.service;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.observers.SaveCategoryObserver;
import org.example.service.snapshot.CategorySnapshot;
import org.example.service.snapshot.SnapshotManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTests {

    private CategoryService service;
    private CategoryRepository repositoryMock;
    private SaveCategoryObserver observerMock;

    @BeforeEach
    void setUp() {
        repositoryMock = mock(CategoryRepository.class);
        observerMock = mock(SaveCategoryObserver.class);
        SnapshotManager<CategorySnapshot> snapshotManagerMock = mock(SnapshotManager.class);
        service = new CategoryService(repositoryMock, observerMock, snapshotManagerMock);
    }

    @Test
    public void getCategory_validId_shouldReturnCategory() {
        var category = new Category(1, "", "");
        when(repositoryMock.get(category.getId())).thenReturn(category);

        var result = service.getCategory(category.getId());

        assertEquals(category, result);
    }

    @Test
    public void getCategory_invalidId_shouldThrowNoSuchElementException() {
        var categoryId = 1;
        when(repositoryMock.get(categoryId)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.getCategory(categoryId));

        assertEquals("Category with id %d not found".formatted(categoryId), result.getMessage());
    }

    @Test
    public void getAllCategories_shouldReturnCategories() {
        var list = List.of(new Category(1, "", ""), new Category(0, "", ""));
        when(repositoryMock.getAll()).thenReturn(list);

        var result = service.getAllCategories();

        assertEquals(list, result);
    }

    @Test
    public void addCategory_shouldSave() {
        var category = new Category(1, "", "");

        service.addCategory(category);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(repositoryMock).save(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void updateCategory_categoryExists_shouldUpdate() {
        var category = new Category(1, "", "");
        when(repositoryMock.update(category)).thenReturn(category);

        service.updateCategory(category);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(repositoryMock).update(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void updateCategory_categoryDoesNotExist_shouldThrowNoSuchElementException() {
        var category = new Category(1, "", "");
        when(repositoryMock.update(category)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.updateCategory(category));

        assertEquals("Category with id %d not found".formatted(category.getId()), result.getMessage());
    }


    @Test
    public void deleteCategory_validId_shouldDelete() {
        var categoryId = 1;
        when(repositoryMock.delete(categoryId)).thenReturn(new Category(categoryId, "", ""));

        service.deleteCategory(categoryId);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(repositoryMock).delete(captor.capture());
        assertEquals(categoryId, captor.getValue());
    }

    @Test
    public void deleteCategory_invalidId_shouldThrowNoSuchElementException() {
        var categoryId = 1;
        when(repositoryMock.delete(categoryId)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.deleteCategory(categoryId));

        assertEquals("Category with id %d not found".formatted(categoryId), result.getMessage());
    }

    @Test
    public void update_onServiceAdd_shouldBeCalled() {

        var category  = new Category(1, "", "");
        service.addCategory(category);

        var captor = ArgumentCaptor.forClass(Category.class);
        verify(observerMock).update(captor.capture());
        assertEquals(category, captor.getValue());
    }
}

