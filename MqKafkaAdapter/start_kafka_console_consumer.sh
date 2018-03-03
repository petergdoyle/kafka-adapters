#!/bin/sh


if [ ! "$(ls -A $kafka_installation_dir)" ]; then
  display_error "Kafka not installed. Cannot continue. Install Kafka!"
  exit 1
fi

if [ -z $KAFKA_HOME ]; then
  echo "No env var KAFKA_HOME is set. Cannot continue. Source your ~/.bash_profile!"
  exit 1
fi

BKR_PIDS=`ps ax | grep -i 'kafka\.Kafka' | grep -v grep | awk '{print $1}'`

if [ ! -z $BKR_PIDS ]; then

  $KAFKA_HOME/bin/kafka-console-consumer.sh --new-consumer --bootstrap-server localhost:9091 --topic kafka-simple-topic-1  --from-beginning --delete-consumer-offsets

else
  display_warn "Broker is not running. Cannot continue. Start the kafka cluster!"
  exit 1
fi
