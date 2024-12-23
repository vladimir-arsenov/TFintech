package org.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.example.client.ApiClient;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.example.model.Category;
import org.example.model.Location;
import org.example.repository.ConcurrentHashMapRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ApplicationStartUpListener {
    private final ConcurrentHashMapRepository<Integer, Category> categoryRepository;
    private final ConcurrentHashMapRepository<String, Location> locationsRepository;
    private final ApiClient apiClient;
    private final ExecutorService fixedExecutor;
    private final ScheduledExecutorService scheduledExecutor;
    private final Duration schedulerInterval;

    public ApplicationStartUpListener(ConcurrentHashMapRepository<Integer, Category> categoryRepository,
                                      ConcurrentHashMapRepository<String, Location> locationsRepository,
                                      ApiClient apiClient,
                                      @Qualifier("fixed-executor") ExecutorService fixedExecutor,
                                      @Qualifier("scheduled-executor") ScheduledExecutorService scheduledExecutor,
                                      @Value("${executors.scheduler.interval}") Duration schedulerInterval
    ) {
        this.categoryRepository = categoryRepository;
        this.locationsRepository = locationsRepository;
        this.apiClient = apiClient;
        this.fixedExecutor = fixedExecutor;
        this.scheduledExecutor = scheduledExecutor;
        this.schedulerInterval = schedulerInterval;
    }

    @LogExecutionTime
    @EventListener(ApplicationReadyEvent.class)
    public void initRepositories() {
        scheduledExecutor.scheduleWithFixedDelay(this::initialize, 0, schedulerInterval.getSeconds(), TimeUnit.SECONDS);
    }

    private void initialize() {

        Runnable categoryInitTask = () -> {
            log.info("Initializing category repository...");
            Arrays.stream(apiClient.getCategories()).forEach(categoryRepository::add);
            log.info("Category repository initialized");
        };

        Runnable locationInitTask = () -> {
            log.info("Initializing location repository...");
            Arrays.stream(apiClient.getLocations()).forEach(locationsRepository::add);
            log.info("Location repository initialized");
        };

        try {
            var futures = List.of(fixedExecutor.submit(categoryInitTask), fixedExecutor.submit(locationInitTask));
            for(var future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while initializing repositories", e);
        }
    }
}
