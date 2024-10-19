package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.ApiClient;
import org.example.model.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class EventService {

    private final ApiClient apiClient;

    public Mono<List<Event>> getEvents(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        return Mono.zip(
                Mono.fromSupplier(() -> apiClient.getEvents(dateFrom, dateTo)),
                Mono.fromSupplier(() -> apiClient.convertMoney(budget, currency)),
                (events, convertedBudget) -> Arrays.stream(events)
                        .filter(event -> event.getPrice() <= convertedBudget)
                        .toList()
        );
    }

    @Async
    public CompletableFuture<List<Event>> getEventsCompletableFuture(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        List<Event> result = new ArrayList<>();

        CompletableFuture<Void> allDone = CompletableFuture.supplyAsync(() -> apiClient.getEvents(dateFrom, dateTo))
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> apiClient.convertMoney(budget, currency)),
                        (Event[] eventsArray, Float b) -> Arrays.stream(eventsArray)
                                .filter(event -> event.getPrice() <= b)
                                .forEach(result::add)
        );

        return allDone.thenApply(v -> new ArrayList<>(result));
    }


}
