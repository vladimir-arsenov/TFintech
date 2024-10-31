package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TFintechApplication {
    public static void main(String[] args) {
        SpringApplication.run(TFintechApplication.class, args);
    }
}
