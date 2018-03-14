/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateConnectionString;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventProcessorHost;
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

//        final String consumerGroupName = ehProperties.getProperty("ConsumerGroupName"); //"$Default";
//        final String serviceBusNamespaceName = ehProperties.getProperty("ServiceBusNamespaceName"); //"----ServiceBusNamespaceName-----";
//        final String eventHubName = ehProperties.getProperty("EventHubName"); //"----EventHubName-----";
//        final String sasKeyName = ehProperties.getProperty("SharedAccessKeyName");//-----SharedAccessSignatureKeyName-----";
//        final String sasKey = ehProperties.getProperty("SharedAccessKey"); //"---SharedAccessSignatureKey----";
//
//        final String storageConnectionString = ehProperties.getProperty("StorageConnectionString");

        EventProcessorHost host = CreateEventProcessorHost(ehProperties);

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
//            host..forceExecutorShutdown(120);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println("End of sample");
    }
}
