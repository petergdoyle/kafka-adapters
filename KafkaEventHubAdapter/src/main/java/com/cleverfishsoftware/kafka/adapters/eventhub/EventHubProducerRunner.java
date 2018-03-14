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

        int numberOfMessages = 1;
        if (args.length > 0) {
            numberOfMessages = Integer.parseInt(args[0]);
        }

        System.out.println("Preparing to send " + numberOfMessages + " to EventHub...");

        final Properties ehProperties = LoadEventHubProperties();
        final EventHubProducer ehProducer = new EventHubProducer(ehProperties);
        for (int i = 0; i < numberOfMessages; i++) {
            ehProducer.send(("test message " + i).getBytes("UTF-8"));
        }
        ehProducer.shutdown();
    }

}
