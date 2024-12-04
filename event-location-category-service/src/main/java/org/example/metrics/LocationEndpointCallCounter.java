package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class LocationEndpointCallCounter {

    private final Counter customMetricCounter;

    public LocationEndpointCallCounter(MeterRegistry meterRegistry) {
        customMetricCounter = Counter.builder("endpoint_calls_counter")
                .description("This metric counts something")
                .tags("some_tag", "some_other_tag")
                .register(meterRegistry);
    }

    public void increment() {
        customMetricCounter.increment();
    }
}
