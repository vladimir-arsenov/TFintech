package org.example.currencyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CurrencyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyServiceApplication.class, args);
    }

}
