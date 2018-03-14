/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 */
public class KafkaEventHubAdapterUtils {

    public static final String KAFKA_CONSUMER_PROPERTIES = "consumer.properties";
    public static final String EVENTHUBS_PROPERTIES = "eventhubs.properties";

    public static String CreateConnectionString(final Properties props) {
        return new ConnectionStringBuilder()
                .setNamespaceName(props.getProperty("ServiceBusNamespaceName"))
                // to target National clouds - use .setEndpoint(URI)
                .setEventHubName(props.getProperty("EventHubName"))
                .setSasKeyName(props.getProperty("SharedAccessKeyName"))
                .setSasKey(props.getProperty("SharedAccessKey"))
                .toString();
    }

    public static EventProcessorHost CreateEventProcessorHost(final Properties props) {
        EventProcessorHost ehHost = new EventProcessorHost(
                EventProcessorHost.createHostName(props.getProperty("StorageHostNamePrefix")),
                props.getProperty("EventHubName"),
                props.getProperty("ConsumerGroupName"),
                CreateConnectionString(props),
                props.getProperty("StorageConnectionString"),
                props.getProperty("StorageContainerName"));
        return ehHost;
    }

    public static Properties LoadEventHubProperties() throws IOException {
        final Properties ehProperties = new Properties();
        LoadDefaults(ehProperties, EVENTHUBS_PROPERTIES);
        ReplaceSystemOverrides(ehProperties);
        return ehProperties;
    }

    public static Properties LoadKafkaConsumerProperties() throws IOException {
        final Properties kafkaProperties = new Properties();
        LoadDefaults(kafkaProperties, KAFKA_CONSUMER_PROPERTIES);
        ReplaceSystemOverrides(kafkaProperties);
        return kafkaProperties;
    }

    public static Properties LoadDefaults(final Properties props, final String resourceName) throws IOException {
        props.load(KafkaEventHubAdapterUtils.class.getClassLoader().getResourceAsStream(resourceName));
        return props;
    }

    public static void ReplaceSystemOverrides(final Properties props) {
        for (Iterator<Object> it = props.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            String property = System.getProperty(key);
            if (property != null) {
                String oldValue = (String) props.put(key, property);
                System.out.println("[WARN] overriding runtime property for " + key + " from " + oldValue + " to " + property);
            }
        }
    }

    public static void Print(Properties props) {
        props.list(System.out);
    }
}
