#!/bin/sh

queue_name="queue1"
broker_url="vm://localhost"
cmd="java -cp .:target/MqKafkaAdapter-1.0-SNAPSHOT.jar com.cleverfishsoftware.kafka.utils.RunJmsMessageSender $queue_name $broker_url"
echo "$cmd"
eval "$cmd"
