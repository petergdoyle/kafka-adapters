/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventHubConnectionString;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 *
 */
public class EventHubProducer {

    private final EventHubClient ehClient;

    public EventHubProducer(Properties ehProperties) throws EventHubException, IOException {
        this.ehClient = EventHubClient.createSync(CreateEventHubConnectionString(ehProperties), Executors.newSingleThreadExecutor());
    }

    public void send(final byte[] payload) throws EventHubException {
        final EventData sendEvent = EventData.create(payload);
        ehClient.sendSync(sendEvent);
    }

    public void shutdown() {
        ehClient.close();
    }

}
