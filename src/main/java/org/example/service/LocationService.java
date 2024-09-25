package org.example.service;

import org.example.model.Location;

import java.util.List;

public interface LocationService {

    Location getLocation(String slug);

    void addLocation(Location location);

    List<Location> getAllLocations() ;

    void updateLocation(Location newLocation) ;

    void deleteLocation(String slug) ;
}
