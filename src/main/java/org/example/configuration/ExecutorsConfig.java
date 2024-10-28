package org.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorsConfig {

    @Value("${executors.executor.threadsCount}")
    private int threadsCount;
    
    @Bean("fixed-executor")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(threadsCount);
    }

    @Bean("scheduled-executor")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
