#!/bin/sh

mvn clean install && java -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar kafka-topic-1 1
