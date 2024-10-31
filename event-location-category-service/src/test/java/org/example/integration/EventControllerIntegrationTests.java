package org.example.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.dto.EventResponse;
import org.example.model.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class EventControllerIntegrationTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("api.url.events", () -> wiremockServer.getBaseUrl() + "/public-api/v1.4/events/?fields=id,title,price");
        registry.add("api.url.currencies", () -> wiremockServer.getBaseUrl() + "/currencies/convert");
    }

    @BeforeAll
    public static void beforeAll() {
        wiremockServer.start();
        configureFor(wiremockServer.getPort());
    }

    @Test
    public void getEvents_shouldReturnEvents() throws Exception {
        var amount = new BigDecimal("20.2533525");
        var events = new Event[]{
                new Event(0, "0","10"),
                new Event(1, "1", "20"),
                new Event(2, "2", "30")
        };
        EventResponse eventResponse = new EventResponse();
        eventResponse.setResults(events);
        String jsonEventResponse = objectMapper.writeValueAsString(eventResponse);
        String jsonAmount = """
                {
                  "fromCurrency": "USD",
                  "toCurrency": "RUB",
                  "convertedAmount": %s
                }
                """.formatted(amount);

        stubFor(
                WireMock.get(urlEqualTo("/public-api/v1.4/events/?fields=id,title,price"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(jsonEventResponse)
                        )
        );
        stubFor(
                WireMock.post(urlEqualTo("/currencies/convert"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(jsonAmount)
                        )
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events")
                        .param("budget", "50.00")
                        .param("currency", "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(0))
                .andExpect(jsonPath("$[0].title").value("0"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].title").value("1"));

    }

    @Test
    public void getEvents_invalidQueryParameters_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events")
                .param("budget", "notANumber")
                .param("cff", "USD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}