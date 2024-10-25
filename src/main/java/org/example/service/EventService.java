package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.model.Event;
import org.example.model.Location;
import org.example.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;

    public List<Event> getAll(String name, Long location, LocalDate fromDate, LocalDate toDate) {
        return eventRepository.findAll(EventRepository.eventSpecification(name, location, fromDate, toDate))
                .stream()
                .toList();
    }

    public Event get(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
    }

    public void add(Event event) {
        eventRepository.save(event);

        Location location = event.getLocation();
        location.getEvents().add(event);
        locationService.update(location.getId(), location);
    }

    public void update(Long id, Event event) {
        var e = get(id);
        e.setDate(event.getDate());
        e.setName(event.getName());
        e.setLocation(event.getLocation());

        eventRepository.save(e);
    }

    public void delete(Long id) {
        eventRepository.delete(get(id));
    }
}
