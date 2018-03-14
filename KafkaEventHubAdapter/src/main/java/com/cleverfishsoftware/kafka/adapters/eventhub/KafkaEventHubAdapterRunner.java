/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadEventHubProperties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.LoadKafkaConsumerProperties;
import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.Print;

/**
 *
 */
public class KafkaEventHubAdapterRunner {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.err.println("\n\n");
            System.err.println("KafkaEventHubAdapter is used as a message bridge between Kafka and the Azure Event Hub. ");
            System.err.println("Messages will be consumed from a Kafka topic and projected onto an EventHub ");
            System.err.println("Configuration of both the Kafka and EventHub clients needs to be specified according to your environment");
            System.err.println("\n");
            System.err.println("Usage:  java [-D OPTION=]... -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar [topics] [other options]");
            System.err.println("");
            System.err.println("Default Kafka Consumer runtime properties are specified under \n"
                    + "src\n"
                    + "├── main\n"
                    + "│   ├── resources\n"
                    + "│       └── consumer.properties");
            System.err.println("Default EventHub runtime properties are specified under \n"
                    + "src\n"
                    + "├── main\n"
                    + "│   ├── resources\n"
                    + "│       └── eventhubs.properties");
            System.err.println("");
            System.err.println("Check the defaults. Any overrides to those runtimes properties can be specified on the command line: ");

            System.err.println("\n");
            System.err.println("Example:  java -DnamespaceName=ServiceBusNamespaceName23 -Dgroup.id=AzureEventHubAdapterGroup -Dbootstrap.servers=engine1:9091 -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar kafka-topic-1,kafka-topic-2 1");

            System.err.println("\n\n");
            System.exit(1);
        }

        final String topicValue = args[0];
        List<String> topics = Arrays.asList(topicValue.split(","));
        System.out.println("[INFO] Kafka topic(s) specified: " + topics);

        final Properties kafkaProperties = LoadKafkaConsumerProperties();
        Print(kafkaProperties);

        final Properties ehProperties = LoadEventHubProperties();
        Print(ehProperties);

        EventHubProducer ehProducer = null;
        try {
            ehProducer = new EventHubProducer(ehProperties);
        } catch (EventHubException ex) {
            System.err.println("[ERROR] " + ex.getMessage());
            System.exit(1);
        }
        
        int cores = Runtime.getRuntime().availableProcessors();
        final ExecutorService executor = Executors.newFixedThreadPool(cores);
        final List<RunnableKafkaEventHubAdapter> consumers = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            RunnableKafkaEventHubAdapter consumer = new RunnableKafkaEventHubAdapter(kafkaProperties, topics, ehProducer);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                consumers.stream().forEach((consumer) -> {
                    consumer.shutdown();
                });
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        });
    }

}
