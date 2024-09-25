package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.example.model.Location;
import org.example.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogExecutionTime
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Location> getLocation(@PathVariable String slug) {
        return ResponseEntity.ok(locationService.getLocation(slug));
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@RequestBody Location location) {
        locationService.addLocation(location);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{slug}")
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        locationService.updateLocation(location);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteLocation(@PathVariable String slug) {
        locationService.deleteLocation(slug);
        return ResponseEntity.noContent().build();
    }
}
