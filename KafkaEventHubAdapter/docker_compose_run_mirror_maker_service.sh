#!/bin/sh

topic='kafka-simple-topic-1'
read -e -p "Enter the topic name: " -i "$topic" topic

cp -vf docker-compose-mirror-maker-template.yml docker-compose.yml
token='${KAFKA_TOPIC}'
sed -i "s/$token/$topic/g" docker-compose.yml

scale='3'
service="kafka-eventhub-adapter-service"
read -e -p "Enter the scale factor for $service: " -i "$scale" scale

detached=''
confirm='y'
read -e -p "Run in background (y/n): " -i "$confirm" confirm
if [ "$confirm" == "y" ]; then
  detached='-d'
fi

cmd="docker-compose up $detached --scale $service=$scale"
echo "$cmd"
eval "$cmd"
