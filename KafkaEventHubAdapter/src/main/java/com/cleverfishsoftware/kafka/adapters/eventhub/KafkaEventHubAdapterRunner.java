/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import static com.cleverfishsoftware.kafka.adapters.eventhub.KafkaEventHubAdapterUtils.CreateEventHubConnectionString;
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
import com.microsoft.azure.eventhubs.EventHubClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 */
public class KafkaEventHubAdapterRunner {

    public static void main(String[] args) throws IOException, EventHubException {

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
        final String ehConnectionString = CreateEventHubConnectionString(ehProperties);

//        https://kafka.apache.org/0102/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html
//         1. One Consumer Per Thread
//
//        A simple option is to give each thread its own consumer instance. Here are the pros and cons of this approach:
//        PRO: It is the easiest to implement
//        PRO: It is often the fastest as no inter-thread co-ordination is needed
//        PRO: It makes in-order processing on a per-partition basis very easy to implement (each thread just processes messages in the order it receives them).
//        CON: More consumers means more TCP connections to the cluster (one per thread). In general Kafka handles connections very efficiently so this is generally a small cost.
//        CON: Multiple consumers means more requests being sent to the server and slightly less batching of data which can cause some drop in I/O throughput.
//        CON: The number of total threads across all processes will be limited by the total number of partitions.
//
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        final List<RunnableKafkaEventHubAdapter> kehAdapterThreads = new ArrayList<>(cores);
        for (int i = 0; i < cores; i++) {
            KafkaConsumer kafkaConsumer = new KafkaConsumer<>(kafkaProperties);
            EventHubClient ehClient = EventHubClient.createSync(ehConnectionString, Executors.newSingleThreadExecutor());
            EventHubProducer ehProducer = new EventHubProducer(ehClient);
            RunnableKafkaEventHubAdapter kehAdapterThread = new RunnableKafkaEventHubAdapter(kafkaConsumer, topics, ehProducer);
            kehAdapterThreads.add(kehAdapterThread);
            executorService.submit(kehAdapterThread);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                kehAdapterThreads.stream().forEach((each) -> {
                    each.shutdown();
                });
                executorService.shutdown();
                try {
                    executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        });
    }

}
