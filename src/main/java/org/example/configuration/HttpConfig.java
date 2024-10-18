package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class HttpConfig {

    private int threadsCount = 2;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("fixed-executor")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(threadsCount);
    }

    @Bean("scheduled-executor")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
