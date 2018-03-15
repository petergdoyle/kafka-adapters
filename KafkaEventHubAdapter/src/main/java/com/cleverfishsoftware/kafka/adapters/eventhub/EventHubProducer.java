/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;

/**
 *
 */
public class EventHubProducer {

    private final EventHubClient ehClient;

    public EventHubProducer(EventHubClient ehClient) {
        this.ehClient = ehClient;
    }

    public void send(final byte[] payload) throws EventHubException {
        final EventData sendEvent = EventData.create(payload);
        ehClient.sendSync(sendEvent);
    }

    public void shutdown() {
        ehClient.close();
    }

}
