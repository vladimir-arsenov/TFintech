package org.example.executiontimeloggerstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class Configuration {

    @Bean
    public LogExecutionTimePostProcessor postProcessor() {
        return new LogExecutionTimePostProcessor();
    }
}
