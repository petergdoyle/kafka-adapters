/*
 */
package com.cleverfishsoftware.kafka.adapters.eventhub;

import com.microsoft.azure.eventhubs.EventHubException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class KafkaEventHubAdapterRunner {

    private static final String KAFKA_CONSUMER_PROPERTIES = "consumer.properties";
    private static final String EVENTHUBS_PROPERTIES = "eventhubs.properties";

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("Usage:  java [-D OPTION=]... -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar [topics] [number of consumer threads]");
            System.err.println("");
            System.err.println("KafkaEventHubAdapter is used as a message bridge between Kafka and the Azure Event Hub. ");
            System.err.println("Messages will be consumed from a Kafka topic and projected onto an EventHub ");
            System.err.println("Default Kafka Consumer properties are specified under \n"
                    + "src\n"
                    + "├── main\n"
                    + "│   ├── resources\n"
                    + "│       └── consumer.properties");
            System.err.println("Overrides to those consumer properties can be specified on the command line: ");
            System.err.println("Example:  java -Dgroup.id=AzureEventHubAdapterGroup -Dbootstrap.servers=engine1:9091 -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar kafka-topic-1,kafka-topic-2 1");
            System.exit(1);
        }

        final String topicValue = args[0];
        List<String> topics = Arrays.asList(topicValue.split(","));
        System.out.println("[INFO] Kafka topic(s) specified: " + topics);

        final String numConsumersValue = args[1];
        int numConsumers = 0;
        try {
            numConsumers = Integer.parseInt(numConsumersValue);
        } catch (NumberFormatException ex) {
            System.err.println("[ERROR] Invalid value specified for 2nd parameter to specify number of consumers. Must be a numeric value");
            System.exit(1);
        }
        System.out.println("[INFO] Number of consumer threads: " + numConsumers);

        final Properties kafkaProperties = new Properties();
        loadDefaults(kafkaProperties, KAFKA_CONSUMER_PROPERTIES);
        replaceSystemOverrides(kafkaProperties);
        print(kafkaProperties);

        final Properties ehProperties = new Properties();
        loadDefaults(ehProperties, EVENTHUBS_PROPERTIES);
        replaceSystemOverrides(kafkaProperties);
        print(ehProperties);

        EventHubProducer ehProducer = null;
        try {
            ehProducer = new EventHubProducer(ehProperties);
        } catch (EventHubException ex) {
            System.err.println("[ERROR] " + ex.getMessage());
            System.exit(1);
        }

        final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
        final List<RunnableKafkaEventHubAdapter> consumers = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
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

    private static Properties loadDefaults(final Properties props, final String resourceName) throws IOException {
        props.load(KafkaEventHubAdapterRunner.class.getClassLoader().getResourceAsStream(resourceName));
        return props;
    }

    private static void replaceSystemOverrides(final Properties props) {
        for (Iterator<Object> it = props.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            String property = System.getProperty(key);
            if (property != null) {
                String oldValue = (String) props.put(key, property);
                System.out.println("[WARN] overriding runtime property for " + key + " from " + oldValue + " to " + property);
            }
        }
    }

    private static void print(Properties kafkaProperties) {
        kafkaProperties.list(System.out);
    }

}
