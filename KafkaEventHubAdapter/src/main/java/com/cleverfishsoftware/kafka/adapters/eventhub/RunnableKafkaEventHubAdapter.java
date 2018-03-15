/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventHubConnectionString;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 */
public class RunnableKafkaEventHubAdapter implements Runnable {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private final List<String> kafkaTopics;
    private final EventHubProducer ehProducer;
    private static final int CONSUMER_POLLING_RATE = 1000;

    public RunnableKafkaEventHubAdapter(final Properties kafkaProperties, final Properties ehProperties, final List<String> kafkaTopics) throws EventHubException, IOException {
        this.kafkaConsumer = new KafkaConsumer(kafkaProperties);
        this.ehProducer = new EventHubProducer(ehProperties);
        this.kafkaTopics = kafkaTopics;
    }

    public void run() {
        try {
            kafkaConsumer.subscribe(kafkaTopics);
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(CONSUMER_POLLING_RATE);
                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    System.out.println("INFO reading " + value);
                    ehProducer.send(value.getBytes("UTF-8"));

//        If  you have to ensure the data consistency, choose commitSync() because it will make sure that, 
//        before doing any further actions, you will know whether the offset commit is successful or failed. 
//        But because it is sync and blocking, you will spend more time on waiting for the commit to be finished, 
//        which leads to high latency.
                    kafkaConsumer.commitSync();

                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            kafkaConsumer.close();
        }
    }

    public void shutdown() {
        kafkaConsumer.wakeup();
    }

}
