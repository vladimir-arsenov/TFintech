package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Location;
import org.example.repository.ConcurrentHashMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
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
        log.info("getLocations() from service");
        return repository.getAll();
    }

    public void updateLocation(Location newLocation) {
        Optional.ofNullable(repository.update(newLocation))
                .orElseThrow(() -> new NoSuchElementException("Location with id %s not found".formatted(newLocation.getSlug())));
    }

    public void deleteLocation(String slug) {
        log.info("deleteLocation() from service");
        Optional.ofNullable(repository.delete(slug))
                .orElseThrow(() -> new NoSuchElementException("Location with id %s not found".formatted(slug)));

    }
}
