package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.client.ApiClient;
import org.example.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes={TFintechApplication.class})
@WireMockTest
public class ApiClientTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiClient apiClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("api.url.locations", () -> wireMockExtension.baseUrl() + "/public-api/v1.4/locations");
        registry.add("api.url.categories", () -> wireMockExtension.baseUrl() + "/public-api/v1.4/place-categories");
    }

    @Test
    public void getLocations_shouldReturnLocations(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {
        Location l = new Location("slug", "name");
        var locations = List.of(new Location("1", " "), new Location("1", " "),
                new Location("1", " "), new Location("1", " "), l);
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

        assertEquals(5, result.length);
    }
}
