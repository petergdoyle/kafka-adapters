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
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class EventHubProducerAsync {

    private final CompletableFuture<EventHubClient> ehClient;
    private final ExecutorService executorService;

    public EventHubProducerAsync(final Properties props) throws EventHubException, IOException {
        final String connectionString = CreateConnectionString(props);

        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
        ehClient = EventHubClient.create(connectionString, executorService);
    }

    public void send(final byte[] payload) throws Exception {
        final EventData eventData = EventData.create(payload);
        ehClient.get().send(eventData);
    }

    public void shutdown() {
        try {
            ehClient.get().close();
        } catch (Exception ex) {
            
        }
    }

}
