package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.example.metrics.LocationEndpointCallCounter;
import org.example.model.Location;
import org.example.service.LocationService;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@LogExecutionTime
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationEndpointCallCounter metric;

    @GetMapping
    public List<Location> getLocations() {
        metric.increment();
        try(var requestId = MDC.putCloseable("requestId", UUID.randomUUID().toString());
            var requestMethod = MDC.putCloseable("requestMethod", "GET"))
        {
            log.info("getLocations() from controller");
            return locationService.getAllLocations();
        }
    }

    @GetMapping("/{slug}")
    public Location getLocation(@PathVariable String slug) {
        return locationService.getLocation(slug);
    }

    @PostMapping
    public void addLocation(@RequestBody Location location) {
        locationService.addLocation(location);
    }

    @PutMapping("/{slug}")
    public void updateLocation(@RequestBody Location location) {
        locationService.updateLocation(location);
    }

    @DeleteMapping("/{slug}")
    public void deleteLocation(@PathVariable String slug) {
        try(var requestId = MDC.putCloseable("requestId", UUID.randomUUID().toString());
            var requestMethod = MDC.putCloseable("requestMethod", "DELETE"))
        {
            log.info("deleteLocations() from controller");
            locationService.deleteLocation(slug);
        }
    }
}
