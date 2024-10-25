package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationDto;
import org.example.mapper.LocationMapper;
import org.example.service.LocationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/locations")
@Validated
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping("")
    public List<LocationDto> getLocations() {
        return locationService.getAll().stream().map(locationMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public LocationDto getLocation(@PathVariable Long id) {
        return locationMapper.toDto(locationService.get(id));
    }

    @PostMapping
    public void addLocation(@Valid @RequestBody LocationDto location) {
        locationService.add(locationMapper.toModel(location));
    }

    @PutMapping("/{id}")
    public void updateLocation(@PathVariable Long id, @Valid @RequestBody LocationDto location) {
        locationService.update(id, locationMapper.toModel(location));
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.delete(id);
    }
}
