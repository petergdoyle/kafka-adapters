/*
 */
package com.cleverfishsoftware.kafka.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.ConnectionFactory;

/**
 *
 */
public class ActiveMQJmsProvider implements JmsProvider {

    final private String messageBrokerUrl;

    public ActiveMQJmsProvider(String messageBrokerUrl) {
        this.messageBrokerUrl = messageBrokerUrl;
    }

    @Override
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory;
        connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        return connectionFactory;
    }

}
