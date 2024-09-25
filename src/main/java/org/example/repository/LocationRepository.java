package org.example.repository;

import org.example.model.Location;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationRepository implements ConcurrentHashMapRepository<String, Location> {

    private final ConcurrentHashMap<String, Location> storage;

    public LocationRepository() {
        storage = new ConcurrentHashMap<>();
    }

    @Override
    public Location get(String id) {
        return storage.get(id);
    }

    @Override
    public void add(Location e) {
        storage.put(e.getSlug(), e);
    }

    @Override
    public List<Location> getAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Location update(Location e) {
        return storage.put(e.getSlug(), e);
    }

    @Override
    public Location delete(String id) {
        return storage.remove(id);
    }
}
