/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import java.util.Properties;

/**
 *
 */
public class EventHubConsumer {

    private final EventProcessorHost host;

    public EventHubConsumer(Properties ehProperties) {
        ConnectionStringBuilder ehConnectionString = EventHubUtils.createConnectionStringBuilder(ehProperties);
        host = new EventProcessorHost(
				EventProcessorHost.createHostName(hostNamePrefix),
				eventHubName,
				consumerGroupName,
				ehConnectionString.toString(),
				storageConnectionString,
				storageContainerName);
    }

}
