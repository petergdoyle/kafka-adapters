/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import java.io.IOException;
import java.util.Properties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadEventHubProperties;

/**
 *
 */
public class EventHubConsumerRunner {

    public static void main(String[] args) throws IOException {

        final Properties ehProperties = LoadEventHubProperties();

        EventHubConsumer ehConsumer = new EventHubConsumer(ehProperties);

    }
}
