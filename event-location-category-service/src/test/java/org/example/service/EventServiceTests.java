package org.example.service;

import org.example.client.ApiClient;
import org.example.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventServiceTests {

    private ApiClient apiClientMock;
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        apiClientMock = mock(ApiClient.class);
        eventService = new EventService(apiClientMock);
    }

    @Test
    public void getEvents_shouldReturnListOfEventsAccordingToBudget() {
        var events = new Event[] {
                new Event(0, "", "10"),
                new Event(1, "","20"),
                new Event(2, "","30")
        };
        var convertedAmount = new BigDecimal("20");
        when(apiClientMock.getEvents(any(), any())).thenReturn(events);
        when(apiClientMock.convertMoney(any(), any())).thenReturn(convertedAmount);

        var resultEvents = eventService.getEvents(null, null, null, null).block();

        assertNotNull(resultEvents);
        assertEquals(2, resultEvents.size());
        assertEquals(resultEvents.get(0), events[0]);
        assertEquals(resultEvents.get(1), events[1]);
    }

    @Test
    public void getEvents_apiClientError_shouldReturnEmptyList() {
        when(apiClientMock.getEvents(any(), any())).thenThrow(RestClientException.class);
        when(apiClientMock.convertMoney(any(), any())).thenReturn(new BigDecimal(0));

        var resultEvents = eventService.getEvents(null, null, null, null).block();

        assertNotNull(resultEvents);
        assertTrue(resultEvents.isEmpty());
    }

    @Test
    public void getEventsCompletableFuture_shouldReturnListOfEventsAccordingToBudget() throws ExecutionException, InterruptedException {
        var events = new Event[] {
                new Event(0, "", "10"),
                new Event(1, "","20"),
                new Event(2, "","30")
        };
        var convertedAmount = new BigDecimal("20");
        when(apiClientMock.getEvents(any(), any())).thenReturn(events);
        when(apiClientMock.convertMoney(any(), any())).thenReturn(convertedAmount);

        var resultEvents = eventService.getEventsCompletableFuture(null, null, null, null).get();

        assertEquals(2, resultEvents.size());
        assertEquals(resultEvents.get(0), events[0]);
        assertEquals(resultEvents.get(1), events[1]);
    }

    @Test
    public void getEventsCompletableFuture_apiClientError_shouldReturnEmptyList() throws ExecutionException, InterruptedException {
        when(apiClientMock.getEvents(any(), any())).thenThrow(RestClientException.class);
        when(apiClientMock.convertMoney(any(), any())).thenReturn(null);

        var resultEvents = eventService.getEventsCompletableFuture(null, null, null, null).get();

        assertTrue(resultEvents.isEmpty());
    }
}
