package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.example.KafkaConfig.TOPIC;
import static org.example.KafkaConfig.consumers;
import static org.example.KafkaConfig.producers;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1)
@Fork(value = 1)
@Measurement(time = 1, iterations = 10)
@State(Scope.Thread)
public class KafkaBenchmarkWithConfigurations {

    @Param({"simple", "load_balancing", "multiple_consumers", "load_balancing_multiple_consumers", "stress_test"})
    public String configType;
    private final int messageCount = 100;


    @Setup(Level.Trial)
    public void setup() {
        int producerCount = 0;
        int consumerCount = 0;

        switch (configType) {
            case "simple" -> {
                producerCount = 1;
                consumerCount = 1;
            }
            case "load_balancing" -> {
                producerCount = 3;
                consumerCount = 1;
            }
            case "multiple_consumers" -> {
                producerCount = 1;
                consumerCount = 3;
            }
            case "load_balancing_multiple_consumers" -> {
                producerCount = 3;
                consumerCount = 3;
            }
            case "stress_test" -> {
                producerCount = 10;
                consumerCount = 10;
            }
        }

        KafkaConfig.setupProducers(producerCount);
        KafkaConfig.setupConsumers(consumerCount);
    }


    @Benchmark
    public void test(Blackhole blackhole) {
        ExecutorService producerExecutor = Executors.newFixedThreadPool(producers.size());
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(consumers.size());

        producers.forEach(producer -> producerExecutor.submit(() -> {
            for (int i = 0; i < messageCount; i++) {
                producer.send(new ProducerRecord<>(TOPIC, "key", "Message " + i));
            }
        }));

        consumers.forEach(consumer -> consumerExecutor.submit(() -> {
            int processedMessages = 0;
            while (processedMessages < messageCount) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                processedMessages += records.count();
                for (ConsumerRecord<String, String> record : records) {
                    blackhole.consume(record);
                }
            }
        }));

        producerExecutor.shutdown();
        consumerExecutor.shutdown();
        try {
            producerExecutor.awaitTermination(5, TimeUnit.SECONDS);
            consumerExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @TearDown(Level.Trial)
    public void teardown() {
        KafkaConfig.close();
    }
}