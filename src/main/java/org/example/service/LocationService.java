package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Location;
import org.example.repository.ConcurrentHashMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final ConcurrentHashMapRepository<String, Location> repository;

    public Location getLocation(String slug) {
        return Optional.ofNullable(repository.get(slug))
                .orElseThrow(() -> new NoSuchElementException("Location with id %s not found".formatted(slug)));
    }

    public void addLocation(Location location) {
        repository.add(location);
    }

    public List<Location> getAllLocations() {
        return repository.getAll();
    }

    public void updateLocation(Location newLocation) {
        Optional.ofNullable(repository.update(newLocation))
                .orElseThrow(() -> new NoSuchElementException("Location with id %s not found".formatted(newLocation.getSlug())));
    }

    public void deleteLocation(String slug) {
        Optional.ofNullable(repository.delete(slug))
                .orElseThrow(() -> new NoSuchElementException("Location with id %s not found".formatted(slug)));

    }
}
