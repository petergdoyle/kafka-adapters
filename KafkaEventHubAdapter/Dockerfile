FROM centos

RUN yum install -y java-1.8.0-openjdk* vim telnet net-tools wireshark

COPY target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar /KafkaEventHubAdapter/target/KafkaEventHubAdapter-1.0-SNAPSHOT.jar
COPY target/lib KafkaEventHubAdapter/target/lib/

WORKDIR /KafkaEventHubAdapter
