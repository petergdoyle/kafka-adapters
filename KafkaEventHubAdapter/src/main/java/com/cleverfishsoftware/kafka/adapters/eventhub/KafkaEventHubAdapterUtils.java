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

    public static final String DEFAULT_KAFKA_CONSUMER_PROPERTIES = "consumer.properties";
    public static final String DEFAULT_EVENTHUBS_PROPERTIES = "eventhubs.properties";

    public static String CreateEventHubConnectionString(final Properties ehProperties) {
        return new ConnectionStringBuilder()
                .setNamespaceName(ehProperties.getProperty("ServiceBusNamespaceName"))
                // to target National clouds - use .setEndpoint(URI)
                .setEventHubName(ehProperties.getProperty("EventHubName"))
                .setSasKeyName(ehProperties.getProperty("SharedAccessKeyName"))
                .setSasKey(ehProperties.getProperty("SharedAccessKey"))
                .toString();
    }

    public static EventProcessorHost CreateEventProcessorHost(final Properties ehProperties) {
        EventProcessorHost ehHost = new EventProcessorHost(
                EventProcessorHost.createHostName(ehProperties.getProperty("StorageHostNamePrefix")),
                ehProperties.getProperty("EventHubName"),
                ehProperties.getProperty("ConsumerGroupName"),
                CreateEventHubConnectionString(ehProperties),
                ehProperties.getProperty("StorageConnectionString"),
                ehProperties.getProperty("StorageContainerName"));
        return ehHost;
    }

    public static Properties LoadEventHubProperties() throws IOException {
        final Properties ehProperties = new Properties();
        LoadDefaults(ehProperties, DEFAULT_EVENTHUBS_PROPERTIES);
        ReplaceSystemOverrides(ehProperties);
        return ehProperties;
    }

    public static Properties LoadKafkaConsumerProperties() throws IOException {
        final Properties kafkaProperties = new Properties();
        LoadDefaults(kafkaProperties, DEFAULT_KAFKA_CONSUMER_PROPERTIES);
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
