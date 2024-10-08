package org.example.service;

import org.example.model.Location;
import org.example.repository.LocationRepository;
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

public class LocationServiceTests {

    private LocationService service;
    private LocationRepository repositoryMock;

    @BeforeEach
    void setUp() {
        repositoryMock = mock(LocationRepository.class);
        service = new LocationService(repositoryMock);
    }

    @Test
    public void getLocation_validId_shouldReturnLocation() {
        var category = new Location("", "");
        when(repositoryMock.get(category.getSlug())).thenReturn(category);

        var result = service.getLocation(category.getSlug());

        assertEquals(category, result);
    }

    @Test
    public void getLocation_invalidId_shouldThrowNoSuchElementException() {
        var locationSlug = "slug";
        when(repositoryMock.get(locationSlug)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.getLocation(locationSlug));

        assertEquals("Location with id %s not found".formatted(locationSlug), result.getMessage());
    }

    @Test
    public void getAllCategories_shouldReturnCategories() {
        var list = List.of(new Location("", ""), new Location("", ""));
        when(repositoryMock.getAll()).thenReturn(list);

        var result = service.getAllLocations();

        assertEquals(list, result);
    }

    @Test
    public void addLocation_shouldSave() {
        var category = new Location("", "");

        service.addLocation(category);

        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(repositoryMock).add(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void updateLocation_categoryExists_shouldUpdate() {
        var category = new Location("", "");
        when(repositoryMock.update(category)).thenReturn(category);

        service.updateLocation(category);

        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(repositoryMock).update(captor.capture());
        assertEquals(category, captor.getValue());
    }

    @Test
    public void updateLocation_categoryDoesNotExist_shouldThrowNoSuchElementException() {
        var category = new Location("", "");
        when(repositoryMock.update(category)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.updateLocation(category));

        assertEquals("Location with id %s not found".formatted(category.getSlug()), result.getMessage());
    }


    @Test
    public void deleteLocation_validId_shouldDelete() {
        var locationSlug = "slug";
        when(repositoryMock.delete(locationSlug)).thenReturn(new Location(locationSlug, ""));

        service.deleteLocation(locationSlug);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(repositoryMock).delete(captor.capture());
        assertEquals(locationSlug, captor.getValue());
    }

    @Test
    public void deleteLocation_invalidId_shouldThrowNoSuchElementException() {
        var locationSlug = "slug";
        when(repositoryMock.delete(locationSlug)).thenReturn(null);

        var result = assertThrows(NoSuchElementException.class, () -> service.deleteLocation(locationSlug));

        assertEquals("Location with id %s not found".formatted(locationSlug), result.getMessage());
    }
}

