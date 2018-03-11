/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.EVENTHUBS_PROPERTIES;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.loadDefaults;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.replaceSystemOverrides;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class EventHubConsumerRunner {

    public static void main(String[] args) throws IOException {

        final Properties ehProperties = new Properties();
        loadDefaults(ehProperties, EVENTHUBS_PROPERTIES);
        replaceSystemOverrides(ehProperties);

        EventHubConsumer ehConsumer = new EventHubConsumer(ehProperties);

    }
}
