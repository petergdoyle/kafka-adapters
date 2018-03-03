#!/bin/sh
. ../install/common.sh

#check if kafka is jdk already
if [ ! `readlink ../install/local/java/default` ]; then
  display_error "you need to install java first"
  exit 1
fi

cp -vf ../install/local/java/default java/

#check if kafka is downloaded already
if [ ! `readlink ../install/local/kafka/default` ]; then
  display_error "you need to install kafka first"
  exit 1
fi

cp -vf ../install/local/kafka/default kafka/

cp -vf 0.10.2.1/ kafka/config
