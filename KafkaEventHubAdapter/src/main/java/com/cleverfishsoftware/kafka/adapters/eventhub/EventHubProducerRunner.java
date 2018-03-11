/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.EVENTHUBS_PROPERTIES;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.loadDefaults;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.print;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterRunner.replaceSystemOverrides;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class EventHubProducerRunner {

    public static void main(String[] args) throws IOException {

        final Properties ehProperties = new Properties();
        loadDefaults(ehProperties, EVENTHUBS_PROPERTIES);
        replaceSystemOverrides(ehProperties);
        print(ehProperties);

        EventHubProducer ehProducer = null;
        try {
            ehProducer = new EventHubProducer(ehProperties);
        } catch (EventHubException ex) {
            System.err.println("[ERROR] " + ex.getMessage());
            System.exit(1);
        }

        for (int i = 0; i < 100; i++) {
            try {
                ehProducer.send(("test message " + i).getBytes("UTF-8"));
            } catch (EventHubException ex) {
                ex.printStackTrace();
            }
        }

    }

}
