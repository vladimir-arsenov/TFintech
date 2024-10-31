package org.example.repository.observers;

import lombok.RequiredArgsConstructor;
import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveLocationObserver implements Observer<Location> {
    private final LocationRepository repository;

    @Override
    public void update(Location entity) {
        repository.save(entity);
    }
}