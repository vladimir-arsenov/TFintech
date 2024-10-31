package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.model.Event;
import org.example.model.Location;
import org.example.repository.EventRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;

    public List<Event> getAll(String name, Long location, LocalDate fromDate, LocalDate toDate) {
        return eventRepository.findAll(eventSpecification(name, location, fromDate, toDate))
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

    private Specification<Event> eventSpecification(String name, Long location, LocalDate fromDate, LocalDate toDate) {
        List<Specification<Event>> specs = new ArrayList<>();
        if (name != null) {
            specs.add((event, query, cb) -> cb.equal(event.get("name"), name));
        }

        if (location != null) {
            specs.add((event, query, cb) -> cb.equal(event.get("location").get("id"), location));
        }

        if (fromDate != null && toDate != null) {
            specs.add((event, query, cb) -> cb.between(event.get("date"), fromDate, toDate));
        } else if (fromDate != null) {
            specs.add((event, query, cb) -> cb.greaterThanOrEqualTo(event.get("date"), fromDate));
        } else if (toDate != null) {
            specs.add((event, query, cb) -> cb.lessThanOrEqualTo(event.get("date"), toDate));
        }

        return specs.stream().reduce(Specification::and).orElse(null);
    }
}
