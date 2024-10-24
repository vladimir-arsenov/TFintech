package org.example.repository;

import org.example.model.Location;
import org.example.repository.hashMapRepository.HashMapLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HashMapLocationRepositoryTests {

    private HashMapLocationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HashMapLocationRepository();
    }

    @Test
    public void get_validSlug_shouldReturnLocation() {
        var location = new Location(null, "slug", "", Collections.emptyList());
        repository.add(location);

        var result = repository.get(location.getSlug());

        assertEquals(location, result);
    }

    @Test
    public void get_invalidSlug_shouldReturnNull() {
        var location = new Location(null, "slug", "", Collections.emptyList());
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
        var locations = List.of(new Location(null, "1", "", Collections.emptyList())   ,
                new Location(null, "2", "", Collections.emptyList()), new Location(null, "3", "", Collections.emptyList()));
        locations.forEach(repository::add);

        var result = repository.getAll();

        assertEquals(locations, result);
    }

    @Test
    public void add_existingLocation_shouldReplaceLocation() {
        var location = new Location(null, "slug", "", Collections.emptyList());
        var newLocation = new Location(null, location.getSlug(), "new", Collections.emptyList());
        repository.add(location);

        repository.add(newLocation);

        assertEquals(newLocation, repository.get(location.getSlug()));
    }

    @Test
    public void add_newLocation_shouldAdd() {
        var location = new Location(null, "slug", "", Collections.emptyList());

        repository.add(location);

        assertEquals(location, repository.get(location.getSlug()));
    }

    @Test
    public void add_nullLocation_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.add(null));
    }

    @Test
    public void update_existingLocation_shouldUpdate() {
        var location = new Location(null, "slug", "", Collections.emptyList());
        var updatedLocation = new Location(null, location.getSlug(), "updated", Collections.emptyList());
        repository.add(location);

        repository.update(updatedLocation);
        var result = repository.get(location.getSlug());

        assertEquals(updatedLocation, result);
    }

    @Test
    public void update_nonExistingLocation_shouldReturnNull() {
        assertNull(repository.update(new Location(null, "slug", "", Collections.emptyList())));
    }

    @Test
    public void update_nullLocation_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.update(null));
    }

    @Test
    public void delete_existingLocation_shouldRemove() {
        var location = new Location(null, "slug", "", Collections.emptyList());
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
