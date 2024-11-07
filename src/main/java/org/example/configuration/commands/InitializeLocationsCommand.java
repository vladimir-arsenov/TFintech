package org.example.configuration.commands;

import lombok.RequiredArgsConstructor;
import org.example.client.ApiClient;
import org.example.service.LocationService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitializeLocationsCommand implements Command {
    private final ApiClient apiClient;
    private final LocationService locationService;

    @Override
    public void execute() {
        Arrays.stream(apiClient.getLocations())
              .forEach(locationService::add);
    }
}