/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.EventHubUtils.createEventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import java.util.Properties;

/**
 *
 */
public class EventHubConsumer {

    // EventProcessorHost is a Java class that simplifies receiving events from Event Hubs by managing persistent 
    // checkpoints and parallel receives from those Event Hubs. Using EventProcessorHost,  you can split events 
    // across multiple receivers, even when hosted in different nodes. This example shows how to use 
    // EventProcessorHost for a single receiver.
    private final EventProcessorHost epHost;
    

    public EventHubConsumer(Properties ehProperties) {
        this.epHost = createEventProcessorHost(ehProperties);
    }

}
