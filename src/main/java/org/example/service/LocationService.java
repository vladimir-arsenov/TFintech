package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location with id " + id + " not found"));
    }

    public void add(Location location) {
        locationRepository.save(location);
    }

    public void update(Long id, Location location) {
        var l = get(id);
        l.setName(location.getName());
        l.setSlug(location.getSlug());
        l.setEvents(location.getEvents());

        locationRepository.save(l);
    }

    public void delete(Long id) {
        locationRepository.delete(get(id));
    }
}
