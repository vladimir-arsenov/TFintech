package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Category;
import org.example.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.url.categories}")
    private String categoriesUrl;

    @Value("${api.url.locations}")
    private String locationsUrl;

    public Category[] getCategories() {
        log.info("Calling API to acquire categories...");
        try {
            ResponseEntity<Category[]> response = restTemplate.getForEntity(categoriesUrl, Category[].class);
            log.info("Categories acquired");

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Couldn't fetch categories: {}", e.getMessage());

            return new Category[0];
        }
    }


    public Location[] getLocations() {
        log.info("Calling API to acquire locations...");
        try {

            ResponseEntity<Location[]> response = restTemplate.getForEntity(locationsUrl, Location[].class);
            log.info("Locations acquired");

            return response.getBody();
        } catch (RestClientException e) {
            log.error("Couldn't fetch locations: {}", e.getMessage());

            return new Location[0];
        }
    }
}
