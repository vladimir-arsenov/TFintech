package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.openjdk.jmh.annotations.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput) // Режим измерения: количество операций в секунду
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Единицы измерения
@State(Scope.Thread)
public class KafkaConsumerBenchmark {

    private KafkaConsumer<String, String> consumer;
    private String topicName = "test-topic";

    @Setup(Level.Trial)
    public void setup() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
    }

    @Benchmark
    public void testConsumerThroughput() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // Получение сообщений
        for (ConsumerRecord<String, String> record : records) {
            // Обработка сообщения
            System.out.println("Received: " + record.value());
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        consumer.close();
    }
}