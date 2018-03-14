#!/bin/sh

topic='kafka-simple-topic-1'
read -e -p "Enter the topic name: " -i "$topic" topic

mvn clean install && java -jar target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar $topic
