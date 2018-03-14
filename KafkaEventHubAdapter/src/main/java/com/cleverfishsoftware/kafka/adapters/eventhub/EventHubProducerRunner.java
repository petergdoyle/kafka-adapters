/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import java.util.Properties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadEventHubProperties;

/**
 *
 */
public class EventHubProducerRunner {

    public static void main(String[] args) throws Exception {

        final Properties ehProperties = LoadEventHubProperties();
        final EventHubProducer ehProducer = new EventHubProducer(ehProperties);
        for (int i = 0; i < 100; i++) {
            ehProducer.send(("test message " + i).getBytes("UTF-8"));
        }
        ehProducer.shutdown();
    }

}
