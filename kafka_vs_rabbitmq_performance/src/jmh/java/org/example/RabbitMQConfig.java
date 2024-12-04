package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMQConfig {

    public static final String QUEUE_NAME = "test-queue-1";
    private static final String HOST = "localhost";
    public static final String EXCHANGE = "test-exchange";
    public static final String ROUTING_KEY = "test-key";

    public static List<Channel> producers = new ArrayList<>();
    public static List<Channel> consumers = new ArrayList<>();
    private static Connection connection;

    public static void setupProducers(int n) throws IOException, TimeoutException {
        shutProducers();
        ensureConnection();
        for (int i = 0; i < n; i++) {
            Channel channel = connection.createChannel();
            producers.add(channel);
        }
    }

    public static void setupConsumers(int n) throws IOException, TimeoutException {
        shutConsumers();
        ensureConnection();
        for (int i = 0; i < n; i++) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, false);
            channel.queueBind(QUEUE_NAME, EXCHANGE, ROUTING_KEY);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            consumers.add(channel);
        }
    }

    public static void close() throws IOException, TimeoutException {
        shutConsumers();
        shutProducers();
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    private static void shutConsumers() throws IOException, TimeoutException {
        for (Channel consumer : consumers) {
            if (consumer.isOpen()) {
                consumer.close();
            }
        }
        consumers.clear();
    }

    private static void shutProducers() throws IOException, TimeoutException {
        for (Channel producer : producers) {
            if (producer.isOpen()) {
                producer.close();
            }
        }
        producers.clear();
    }

    private static void ensureConnection() throws IOException, TimeoutException {
        if (connection == null || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            connection = factory.newConnection();
        }
    }
}