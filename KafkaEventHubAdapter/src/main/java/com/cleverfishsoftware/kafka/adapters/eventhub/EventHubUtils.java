/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import java.util.Properties;

/**
 *
 */
public class EventHubUtils {

    public static ConnectionStringBuilder createConnectionStringBuilder(final Properties props) {
        return new ConnectionStringBuilder()
                .setNamespaceName(props.getProperty("ServiceBusNamespaceName"))
                // to target National clouds - use .setEndpoint(URI)
                .setEventHubName(props.getProperty("EventHubName"))
                .setSasKeyName(props.getProperty("SharedAccessKeyName"))
                .setSasKey(props.getProperty("SharedAccessKey"));
    }

    public static EventProcessorHost createEventProcessorHost(final Properties props) {
        EventProcessorHost ehHost = new EventProcessorHost(
                EventProcessorHost.createHostName(props.getProperty("StorageHostNamePrefix")),
                props.getProperty("EventHubName"),
                props.getProperty("ConsumerGroupName"),
                EventHubUtils.createConnectionStringBuilder(props).toString(),
                props.getProperty("StorageConnectionString"),
                props.getProperty("StorageContainerName"));
        return ehHost;
    }
}
