package org.example.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.configuration.ApplicationStartUpListener;
import org.example.controller.LocationController;
import org.example.model.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.NoSuchElementException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class LocationControllerIntegrationTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocationController locationController;

    @Autowired
    private ApplicationStartUpListener applicationStartUpListener;

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("api.url.locations", () -> wiremockServer.getBaseUrl() + "/public-api/v1.4/locations");
        registry.add("api.url.categories", () -> wiremockServer.getBaseUrl() + "/public-api/v1.4/place-categories");
    }

    @BeforeAll
    public static void beforeAll() {
        wiremockServer.start();
        configureFor(wiremockServer.getPort());
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException {

        Location[] locations = { new Location("msk", " "), new Location("2", " "),
                new Location("3", " "), new Location("4", " ")};
        String json = objectMapper.writeValueAsString(locations);
        stubFor(
                WireMock.get(urlEqualTo("/public-api/v1.4/locations"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(json)
                        )
        );
        applicationStartUpListener.initRepositories();
    }

    @Test
    public void shouldReturnLocations() throws JsonProcessingException {
        var result = locationController.getLocations();

        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldGetLocation(){
        var location = locationController.getLocation("msk");

        assertEquals("msk", location.getSlug());
    }

    @Test
    public void shouldAddLocation() {
        var newLocation = new Location("new", " ");
        locationController.addLocation(newLocation);

        var result = locationController.getLocation("new");

        assertEquals(newLocation, result);
    }

    @Test
    public void shouldUpdateLocation() {
        locationController.updateLocation(new Location("msk", "updated"));

        var result = locationController.getLocation("msk");

        assertEquals("updated", result.getName());
    }

    @Test
    public void shouldDeleteLocation() {
        locationController.deleteLocation("msk");

        assertThrows(NoSuchElementException.class, () -> locationController.getLocation("msk"));
    }
}
