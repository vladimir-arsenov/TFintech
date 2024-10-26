package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.ApiClient;
import org.example.model.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final ApiClient apiClient;

    public Mono<List<Event>> getEvents(BigDecimal budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        return Mono.zip(
                Mono.fromSupplier(() -> apiClient.getEvents(dateFrom, dateTo)),
                Mono.fromSupplier(() -> apiClient.convertMoney(budget, currency)),
                (events, convertedBudget) -> Arrays.stream(events)
                        .filter(event -> event.getPrice().compareTo(convertedBudget) <= 0)
                        .toList()
        ).onErrorResume(ex -> {
            log.error("Error while getting events: {}", ex.getMessage());
            return Mono.just(Collections.emptyList());
        });
    }

    @Async
    public CompletableFuture<List<Event>> getEventsCompletableFuture(BigDecimal budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        List<Event> result = new ArrayList<>();

        CompletableFuture<Void> allDone = CompletableFuture
                .supplyAsync(() -> apiClient.getEvents(dateFrom, dateTo))
                .exceptionally(ex -> {
                    log.error("Error while getting events: {}", ex.getMessage());
                    return new Event[0];
                })
                .thenAcceptBoth(CompletableFuture
                                .supplyAsync(() -> apiClient.convertMoney(budget, currency))
                                .exceptionally(ex -> {
                                    log.error("Error while converting money: {}", ex.getMessage());
                                    return BigDecimal.ZERO;
                                }),
                        (events, convertedBudget) -> result.addAll(Arrays.stream(events)
                                .filter(event -> event.getPrice().compareTo(convertedBudget) <= 0)
                                .toList())
        );

        return allDone.thenApply(v -> new ArrayList<>(result));
    }


}
