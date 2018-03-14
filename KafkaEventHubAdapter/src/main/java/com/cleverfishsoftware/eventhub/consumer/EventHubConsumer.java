/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import java.util.Properties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventProcessorHost;

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
        this.epHost = CreateEventProcessorHost(ehProperties);
    }

}
