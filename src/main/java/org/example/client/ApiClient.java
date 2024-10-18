package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Category;
import org.example.model.Event;
import org.example.model.Location;
import org.example.model.Reponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.url.categories}")
    private String categoriesUrl;

    @Value("${api.url.locations}")
    private String locationsUrl;

    @Value("${api.url.events}")
    private String eventsUrl;

    @Value("${api.url.currencies}")
    private String currenciesUrl;

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

    public Event[] getEvents(LocalDate dateFrom, LocalDate dateTo) {
        log.info("Calling API to acquire events...");
        try {
            var url = eventsUrl + (dateFrom == null ? "" : "&actual_since=" + dateFrom) + (dateTo == null ? "" : "&actual_until=" + dateTo);
            System.out.println(url);
            ResponseEntity<Reponse> response = restTemplate.getForEntity(url, Reponse.class);
            log.info("Events acquired");

            return response.getBody().getResults();
        } catch (RestClientException e) {
            log.error("Couldn't fetch events: {}", e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public Float convertMoney(Integer budget, String currency) {
//        log.info("Calling API to convert money...");
//        try {
//            ResponseEntity<ConvertedMoney> response = restTemplate.postForEntity(currenciesUrl, new ConvertionRequest() ,ConvertedMoney.class);
//            log.info("Events acquired");
//
//            return response.getBody().getAmount();
//        } catch (RestClientException e) {
//            log.error("Couldn't fetch events: {}", e.getMessage());
//
//            return 0f;
//        }

        return 100000000f;
    }
}
