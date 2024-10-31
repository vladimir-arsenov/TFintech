package org.example.repository;

import org.example.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocationRepositoryTests {

    private LocationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LocationRepository();
    }

    @Test
    public void get_validSlug_shouldReturnLocation() {
        var location = new Location("slug", "");
        repository.add(location);

        var result = repository.get(location.getSlug());

        assertEquals(location, result);
    }

    @Test
    public void get_invalidSlug_shouldReturnNull() {
        var location = new Location("slug", "");
        repository.add(location);

        var result = repository.get(location.getSlug() + "1");

        assertNull(result);
    }

    @Test
    public void get_nullSlug_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.get(null));
    }

    @Test
    public void getAll_shouldReturnAllLocations() {
        var locations = List.of(new Location("1", ""),
                new Location("2", ""), new Location("3", ""));
        locations.forEach(repository::add);

        var result = repository.getAll();

        assertEquals(locations, result);
    }

    @Test
    public void add_existingLocation_shouldReplaceLocation() {
        var location = new Location("slug", "");
        var newLocation = new Location(location.getSlug(), "new");
        repository.add(location);

        repository.add(newLocation);

        assertEquals(newLocation, repository.get(location.getSlug()));
    }

    @Test
    public void add_newLocation_shouldAdd() {
        var location = new Location("slug", "");

        repository.add(location);

        assertEquals(location, repository.get(location.getSlug()));
    }

    @Test
    public void add_nullLocation_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.add(null));
    }

    @Test
    public void update_existingLocation_shouldUpdate() {
        var location = new Location("slug", "");
        var updatedLocation = new Location(location.getSlug(), "updated");
        repository.add(location);

        repository.update(updatedLocation);
        var result = repository.get(location.getSlug());

        assertEquals(updatedLocation, result);
    }

    @Test
    public void update_nonExistingLocation_shouldReturnNull() {
        assertNull(repository.update(new Location("slug", "")));
    }

    @Test
    public void update_nullLocation_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.update(null));
    }

    @Test
    public void delete_existingLocation_shouldRemove() {
        var location = new Location("slug", "");
        repository.add(location);

        var result = repository.delete(location.getSlug());

        assertEquals(location, result);
        assertNull(repository.get(location.getSlug()));
    }

    @Test
    public void delete_nonExistingLocation_shouldReturnNull() {
        assertNull(repository.delete("slug"));
    }

    @Test
    public void delete_nullLocation_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.delete(null));
    }
}
