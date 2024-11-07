package org.example.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.commands.InitializeCategoriesCommand;
import org.example.executiontimeloggerstarter.LogExecutionTime;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationStartUpListener {
    private final InitializeCategoriesCommand initializeCategoriesCommandCommand;


    @LogExecutionTime
    @EventListener(ApplicationReadyEvent.class)
    public void initRepositories() {
        log.info("Initializing category repository...");

        initializeCategoriesCommandCommand.execute();

        log.info("Category repository initialized");
    }
}
