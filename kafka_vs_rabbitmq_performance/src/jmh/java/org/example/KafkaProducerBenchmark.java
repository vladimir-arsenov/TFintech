package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.openjdk.jmh.annotations.*;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class KafkaProducerBenchmark {

    private KafkaProducer<String, String> producer;
    private String topicName = "test-topic";

    @Setup(Level.Trial)
    public void setup() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(props);
    }

    @Benchmark
    public void testProducerLatency() throws Exception {
        long startTime = System.nanoTime();
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "key", "value");
        RecordMetadata metadata = producer.send(record).get();
        long latency = System.nanoTime() - startTime;
        System.out.println("Latency (ms): " + TimeUnit.NANOSECONDS.toMillis(latency));
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        producer.close();
    }
}