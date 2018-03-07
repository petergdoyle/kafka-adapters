/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.Properties;
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
                .setNamespaceName((String) props.get("namespaceName")) // to target National clouds - use .setEndpoint(URI)
                .setEventHubName((String) props.get("eventHubName"))
                .setSasKeyName((String) props.get("sasKeyName"))
                .setSasKey((String) props.get("sasKey"));
        executorService = Executors.newSingleThreadExecutor();
        ehClient = EventHubClient.createSync(connStr.toString(), executorService);
    }

    public void send(final byte[] payload) throws EventHubException {
        EventData sendEvent = EventData.create(payload);
        ehClient.sendSync(sendEvent);
    }

}
