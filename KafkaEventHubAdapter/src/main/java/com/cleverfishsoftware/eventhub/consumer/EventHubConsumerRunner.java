/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventProcessorHost;
import java.io.IOException;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadEventHubProperties;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class EventHubConsumerRunner {

    public static void main(String[] args) throws IOException {

        EventProcessorHost host = CreateEventProcessorHost(LoadEventHubProperties());
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
//            host.forceExecutorShutdown(120);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println("End of sample");
    }
}
