package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.ApiClient;
import org.example.model.Event;
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

    public List<Event> getEvents(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        Mono<Event[]> eventsMono = Mono.fromSupplier(() -> apiClient.getEvents(dateFrom, dateTo));
        Mono<Float> converetedBudgetMono = Mono.fromSupplier(() -> apiClient.convertMoney(budget, currency));

        return Mono.zip(eventsMono, converetedBudgetMono)
                .map(zip -> Arrays.stream(zip.getT1())
                        .filter(event -> event.getPrice()<= zip.getT2())
                        .toList())
                .block();
    }

    public List<Event> getEventsCompletableFuture(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        List<Event> eventsList = new ArrayList<>();
        CompletableFuture.supplyAsync(() -> apiClient.getEvents(dateFrom, dateTo))
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> apiClient.convertMoney(budget, currency)),
                        (Event[] events, Float convertedBudget) -> Arrays.stream(events)
                                .filter(event -> event.getPrice() <= convertedBudget)
                                .forEach(eventsList::add)
                );
        return eventsList;
    }


}
