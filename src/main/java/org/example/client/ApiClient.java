package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.EventResponse;
import org.example.model.Category;
import org.example.model.Event;
import org.example.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
            ResponseEntity<EventResponse> response = restTemplate.getForEntity(url, EventResponse.class);
            log.info("Events acquired");

            return response.getBody().getResults();
        } catch (RestClientException e) {
            log.error("Couldn't fetch events: {}", e.getMessage());

            return new Event[0];
        }
    }

    public BigDecimal convertMoney(BigDecimal budget, String currency) {
        log.info("Calling API to convert money...");

        Map<String, Object> request = new HashMap<>();
        request.put("fromCurrency", currency);
        request.put("toCurrency", "RUB");
        request.put("amount", budget);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request);

        try {
            var converted = String.valueOf(restTemplate.exchange(currenciesUrl, HttpMethod.POST, entity, Map.class)
                    .getBody().get("convertedAmount"));
            log.info("Money are converted");
            return new BigDecimal(converted);
        } catch (RestClientException e) {
            log.error("Couldn't convert money: {}", e.getMessage());
            return new BigDecimal(0);
        }
    }
}
