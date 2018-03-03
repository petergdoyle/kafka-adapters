/*
 */
package com.cleverfishsoftware.kafka.utils;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 */
public class KafkaMessageSender {

    private final String topic;
    private final KafkaProducer<String, String> producer;

    public KafkaMessageSender(final Properties properties, final String topic) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(properties);
    }

    public void send(final String msg) {
        producer.send(new ProducerRecord<>(topic, msg));
    }
}
