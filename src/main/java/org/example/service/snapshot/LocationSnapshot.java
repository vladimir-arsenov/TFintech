package org.example.service.snapshot;

import lombok.Getter;
import org.example.model.Event;
import org.example.model.Location;

import java.util.List;

@Getter
public class LocationSnapshot {
    private final Long id;
    private final String name;
    private final String slug;
    private final List<Event> events;

    public LocationSnapshot(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.slug = location.getSlug();
        this.events = location.getEvents();
    }
}