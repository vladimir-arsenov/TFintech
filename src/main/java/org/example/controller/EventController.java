package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Event;
import org.example.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<Event> getEvents(
            @RequestParam Integer budget, @RequestParam String currency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
//        return eventService.getEventsCompletableFuture(budget, currency, dateFrom, dateTo).join();
        return eventService.getEvents(budget, currency, dateFrom, dateTo).block();
    }
}
