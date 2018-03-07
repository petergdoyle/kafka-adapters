/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 */
public class RunnableKafkaEventHubAdapter implements Runnable {

    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private final EventHubProducer ehProducer;
    private static final int CONSUMER_POLLING_RATE = 1000;

    public RunnableKafkaEventHubAdapter(final Properties props, final List<String> topics, final EventHubProducer ehProducer) {
        this.topics = new ArrayList<>(topics.size());
        topics.stream().forEach((each) -> {
            this.topics.add(each);
        });
        this.consumer = new KafkaConsumer<>(props);
        this.ehProducer = ehProducer;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(CONSUMER_POLLING_RATE);
                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    ehProducer.send(value.getBytes("UTF-8"));
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }

}
