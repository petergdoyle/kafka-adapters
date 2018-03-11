/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import java.util.Properties;

/**
 *
 */
public class EventHubUtils {

    public static ConnectionStringBuilder createConnectionStringBuilder(final Properties props) {
        return new ConnectionStringBuilder()
                .setNamespaceName((String) props.get("ServiceBusNamespaceName")) // to target National clouds - use .setEndpoint(URI)
                .setEventHubName((String) props.get("EventHubName"))
                .setSasKeyName((String) props.get("SharedAccessKeyName"))
                .setSasKey((String) props.get("SharedAccessKey"));
    }
}
