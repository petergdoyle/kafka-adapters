/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class EventHubProducer {

    private final Properties props;
    private final ConnectionStringBuilder connStr;
    private final EventHubClient ehClient;
    final ExecutorService executorService;

    public EventHubProducer(final Properties props) throws EventHubException, IOException {
        this.props = props;
        connStr = new ConnectionStringBuilder()
                .setNamespaceName((String) props.get("ServiceBusNamespaceName")) // to target National clouds - use .setEndpoint(URI)
                .setEventHubName((String) props.get("EventHubName"))
                .setSasKeyName((String) props.get("SharedAccessKeyName"))
                .setSasKey((String) props.get("SharedAccessKey"));
        executorService = Executors.newSingleThreadExecutor();
        ehClient = EventHubClient.createSync(connStr.toString(), executorService);
    }

    public void send(final byte[] payload) throws EventHubException {
        EventData sendEvent = EventData.create(payload);
        ehClient.sendSync(sendEvent);
    }
    public void shutdown() {
        ehClient.close();
    }

}
