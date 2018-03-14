/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateConnectionString;

/**
 *
 */
public class EventHubProducer {

    private final EventHubClient ehClient;
    private final ExecutorService executorService;

    public EventHubProducer(final Properties props) throws EventHubException, IOException {
        final String connectionString = CreateConnectionString(props);
        executorService = Executors.newSingleThreadExecutor();
        ehClient = EventHubClient.createSync(connectionString, executorService);
    }

    public void send(final byte[] payload) throws EventHubException {
        final EventData sendEvent = EventData.create(payload);
        ehClient.sendSync(sendEvent);
    }

    public void shutdown() {
        ehClient.close();
    }

}
