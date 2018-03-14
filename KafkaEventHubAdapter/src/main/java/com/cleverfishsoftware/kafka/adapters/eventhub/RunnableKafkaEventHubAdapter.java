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
                    System.out.println("INFO reading " + value);
                    ehProducer.send(value.getBytes("UTF-8"));
//        If  you have to ensure the data consistency, choose commitSync() because it will make sure that, 
//        before doing any further actions, you will know whether the offset commit is successful or failed. 
//        But because it is sync and blocking, you will spend more time on waiting for the commit to be finished, 
//        which leads to high latency.
                    consumer.commitSync();
//        If you are ok of certain data inconsistency and want to have low latency, choose commitAsync() because
//        it will not wait to be finished. Instead, it will just send out the commit request and handle the response 
//        from Kafka (success or failure) later, and meanwhile, your code will continue executing.
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
