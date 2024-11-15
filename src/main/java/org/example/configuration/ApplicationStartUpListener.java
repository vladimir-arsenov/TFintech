package org.example.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.commands.InitializeCategoriesCommand;
import org.example.configuration.commands.InitializeLocationsCommand;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartUpListener {
    private final InitializeCategoriesCommand initializeCategoriesCommand;
    private final InitializeLocationsCommand initializeLocationsCommand;

    @LogExecutionTime
    @EventListener(ApplicationReadyEvent.class)
    public void initRepositories() {
        log.info("Initializing category repository...");
        initializeCategoriesCommand.execute();
        log.info("Category repository initialized");

        log.info("Initializing location repository...");
        initializeLocationsCommand.execute();
        log.info("Location repository initialized");
    }
}
