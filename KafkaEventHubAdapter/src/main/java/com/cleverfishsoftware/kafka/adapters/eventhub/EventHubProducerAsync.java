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
public class EventHubProducerAsync {

    private final EventHubClient ehClient;
    private final ExecutorService executorService;

    public EventHubProducerAsync(final Properties props) throws EventHubException, IOException {
        final String connectionString = CreateConnectionString(props);

        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
        ehClient = EventHubClient.createSync(connectionString, executorService);
    }

    public void send(final byte[] payload) throws EventHubException {
        final EventData sendEvent = EventData.create(payload);
        ehClient.send(sendEvent);
    }

    public void shutdown() {
        ehClient.close();
    }

}
