package org.example.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.LocationDto;
import org.example.model.Event;
import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationMapper {

    private final LocationRepository locationRepository;

    public LocationMapper(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location toModel(LocationDto locationDto) {
        List<Event> events = locationDto.getEvents();
        if (events == null && locationDto.getId() != null) {
            events = locationRepository.findById(locationDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Location with id " + locationDto.getId() + " not found"))
                    .getEvents();
        }
        return new Location(null, locationDto.getSlug(), locationDto.getName(), events);
    }

    public LocationDto toDto(Location location) {
        return new LocationDto(location.getId(), location.getSlug(), location.getName(), location.getEvents());
    }
}
