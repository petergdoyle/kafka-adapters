#!/bin/sh

mvn clean install

no_cache=""
if [ "$1" == "--clean" ]; then
  no_cache="--no-cache"
fi

docker build $no_cache -t="mycompany/kafkaeventhubadapter" .
