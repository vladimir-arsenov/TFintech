package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Category;
import org.example.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        ResponseEntity<Category[]> response = restTemplate.getForEntity(categoriesUrl, Category[].class);
        return response.getBody();
    }


    public Location[] getLocations() {
        log.info("Calling API to acquire locations...");
        ResponseEntity<Location[]> response = restTemplate.getForEntity(locationsUrl, Location[].class);
        return response.getBody();
    }
}
