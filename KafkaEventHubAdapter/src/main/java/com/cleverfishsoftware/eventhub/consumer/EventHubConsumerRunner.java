/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import java.io.IOException;
import java.util.Properties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadEventHubProperties;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class EventHubConsumerRunner {

    public static void main(String[] args) throws IOException {

        final Properties ehProperties = LoadEventHubProperties();

        EventHubConsumer ehConsumer = new EventHubConsumer(ehProperties);

        final String consumerGroupName = "$Default";
        final String namespaceName = "----ServiceBusNamespaceName-----";
        final String eventHubName = "----EventHubName-----";
        final String sasKeyName = "-----SharedAccessSignatureKeyName-----";
        final String sasKey = "---SharedAccessSignatureKey----";

        final String storageAccountName = "---StorageAccountName----";
        final String storageAccountKey = "---StorageAccountKey----";
        final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + storageAccountName + ";AccountKey=" + storageAccountKey;

        EventProcessorHost host = new EventProcessorHost(eventHubName, consumerGroupName, eventHubConnectionString.toString(), storageConnectionString);

        System.out.println("Registering host named " + host.getHostName());
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());
        try {
            host.registerEventProcessor(EventProcessor.class, options).get();
        } catch (Exception e) {
            System.out.print("Failure while registering: ");
            if (e instanceof ExecutionException) {
                Throwable inner = e.getCause();
                System.out.println(inner.toString());
            } else {
                System.out.println(e.toString());
            }
        }

        System.out.println("Press enter to stop");
        try {
            System.in.read();
            host.unregisterEventProcessor();

            System.out.println("Calling forceExecutorShutdown");
            host.forceExecutorShutdown(120);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println("End of sample");
    }
}
