#!/bin/sh


topic_name="kafka-simple-topic-1"
broker_url="localhost:9091"
cmd="java -cp .:target/MqKafkaAdapter-1.0-SNAPSHOT.jar com.cleverfishsoftware.kafka.utils.RunJmsKafkaAdapter $topic_name $broker_url"
echo "$cmd"
eval "$cmd"
