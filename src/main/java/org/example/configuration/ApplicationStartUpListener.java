package org.example.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.ApiClient;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.example.model.Category;
import org.example.model.Location;
import org.example.repository.hashMapRepository.ConcurrentHashMapRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartUpListener {
    private final ConcurrentHashMapRepository<Integer, Category> categoryRepository;
    private final ConcurrentHashMapRepository<String, Location> locationsRepository;
    private final ApiClient apiClient;

    @LogExecutionTime
    @EventListener(ApplicationReadyEvent.class)
    public void initRepositories() {
        log.info("Initializing category repository...");

        Arrays.stream(apiClient.getCategories())
                .forEach(categoryRepository::add);

        log.info("Category repository initialized");
        log.info("Initializing location repository...");

        Arrays.stream(apiClient.getLocations())
                .forEach(locationsRepository::add);

        log.info("Location repository initialized");
    }
}
