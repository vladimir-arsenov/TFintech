package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConfig {

    public static final String TOPIC = "test-topic";
    private static final String GROUP_ID = "test-group";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static List<KafkaProducer<String, String>> producers = new ArrayList<>();
    public static List<KafkaConsumer<String, String>> consumers = new ArrayList<>();


    public static void setupProducers(int n) {
        shutProducers();
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        for(int i = 0; i < n; i++) {
            producers.add(new KafkaProducer<>(properties));
        }
    }

    public static void setupConsumers(int n) {
        shutConsumers();
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        for(int i = 0; i < n; i++) {
            var consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Collections.singletonList(TOPIC));
            consumers.add(consumer);
        }
    }
    public static void close() {
        shutConsumers();
        shutProducers();
    }

    private static void shutConsumers() {
        consumers.forEach(KafkaConsumer::close);
        consumers.clear();
    }

    private static void shutProducers() {
        producers.forEach(KafkaProducer::close);
        producers.clear();
    }

}
