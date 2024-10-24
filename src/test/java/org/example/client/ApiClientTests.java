package org.example.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.model.Category;
import org.example.model.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class ApiClientTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiClient apiClient;

    @BeforeAll
    public static void setUp() {
        configureFor(wireMockExtension.getPort());
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("api.url.locations", () -> wireMockExtension.baseUrl() +"/public-api/v1.4/locations");
        registry.add("api.url.categories", () -> wireMockExtension.baseUrl() + "/public-api/v1.4/place-categories");
    }

    @Test
    public void getLocations_shouldReturnLocations() throws JsonProcessingException {
        Location[] locations = { new Location(null, "1", " ", Collections.emptyList()), new Location(null, "2", " ",Collections.emptyList()),
                new Location(null, "3", " ",Collections.emptyList()), new Location(null, "4", " ",Collections.emptyList()) };
        String json = objectMapper.writeValueAsString(locations);

        stubFor(
                WireMock.get(urlEqualTo("/public-api/v1.4/locations"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(json)
                        )
        );

        var result = apiClient.getLocations();
        assertArrayEquals(result, locations);
    }

    @Test
    public void getCategories_shouldReturnCategories() throws JsonProcessingException {
        Category[] categories = { new Category(1, "", " "), new Category(2, "", " "),
                new Category(3, "", " "), new Category(4, "", " ")};
        String json = objectMapper.writeValueAsString(categories);

        stubFor(
                WireMock.get(urlEqualTo("/public-api/v1.4/place-categories"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(json)
                        )
        );

        var result = apiClient.getCategories();
        assertArrayEquals(result, categories);
    }
}
