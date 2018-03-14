#!/bin/sh

mvn clean install && java -cp .:target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar com.cleverfishsoftware.eventhub.consumer.EventHubConsumerRunner
