package org.example.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.dto.EventDto;
import org.example.model.Event;
import org.example.model.Location;
import org.example.service.LocationService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventMapper {

    private final LocationService locationService;

    public Event toModel(EventDto eventDto) {
        try {
            Location location = locationService.get(eventDto.getLocationId());
            return new Event(null, eventDto.getName(), eventDto.getDate(), location);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("Invalid locationId");
        }
    }

    public EventDto toDto(Event event) {
        return new EventDto(event.getId(), event.getName(), event.getDate(), event.getLocation().getId());
    }
}
