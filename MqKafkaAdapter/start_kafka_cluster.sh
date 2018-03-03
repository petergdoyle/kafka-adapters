#!/bin/sh
. ./common.sh

if [ ! "$(ls -A $kafka_installation_dir)" ]; then
  display_error "Kafka not installed. Cannot continue. Install Kafka!"
  exit 1
fi

if [ -z $KAFKA_HOME ]; then
  echo "No env var KAFKA_HOME is set. Cannot continue. Source your ~/.bash_profile!"
  exit 1
fi

# start zookeeper

ZK_PIDS=`ps ax | grep -i QuorumPeerMain | grep -v grep | awk '{print $1}'`
ZK_PID_CNT=`ps ax | grep -i QuorumPeerMain | grep -v grep | awk '{print $1}'| wc -l`

if [ $ZK_PID_CNT -gt 0 ]; then
  display_error "Zookeeper is already running. Cannot contiue. Kill the cluster first!"
  exit 1
fi

kafka_cmd="$KAFKA_HOME/bin/zookeeper-server-start.sh"
kafka_cmd_props_file="$KAFKA_HOME/config/zookeeper.properties"
kafka_log_file="zookeeper-console.log"
start_cmd="$kafka_cmd $kafka_cmd_props_file >$kafka_log_file  2>&1"
echo $start_cmd
response="y"
read -e -p "About to start kafka-zookeeper, continue? " -i $response response
if [ "$response" != "y" ]; then
  exit 1
fi
eval "$start_cmd" &
sleep 2

ZK_PIDS=`ps ax | grep -i QuorumPeerMain | grep -v grep | awk '{print $1}'`

if [ ! -z $ZK_PIDS ]; then
  display_info "Zookeeper process(es) running:\n$ZK_PIDS"
  timeout 5s tail -f $kafka_log_file
else
  display_warn "Zookeeper failed to start. Check the log file $kafka_log_file . Cannot continue"
  exit 1
fi

# start a broker

BKR_PIDS=`ps ax | grep -i 'kafka\.Kafka' | grep -v grep | awk '{print $1}'`
BKR_PID_CNT=`ps ax | grep -i 'kafka\.Kafka' | grep -v grep | awk '{print $1}'| wc -l`

display_info "$BKR_PID_CNT broker process running."
if [ $BKR_PID_CNT -gt 0 ]; then
  display_error "Broker is already running. Cannot contiue. Kill the cluster first!"
  exit 1
fi

kafka_cmd="$KAFKA_HOME/bin/kafka-server-start.sh"
kafka_cmd_props_file="$KAFKA_HOME/config/broker.properties"
kafka_log_file="broker-console.log"
start_cmd="$kafka_cmd $kafka_cmd_props_file >$kafka_log_file  2>&1"
echo $start_cmd
response="y"
read -e -p "About to start kafka-broker, continue? " -i $response response
if [ "$response" != "y" ]; then
  exit 1
fi
eval "$start_cmd" &
sleep 2

BKR_PIDS=`ps ax | grep -i 'kafka\.Kafka' | grep -v grep | awk '{print $1}'`

if [ ! -z $BKR_PIDS ]; then
  display_info "Broker process(es) running:\n$ZK_PIDS"
  timeout 5s tail -f $kafka_log_file
else
  display_warn "Broker failed to start. Check the log file $kafka_log_file . Cannot continue"
  exit 1
fi
