/*
 */
package com.cleverfishsoftware.kafka.utils;

import javax.jms.ConnectionFactory;

/**
 *
 */
public interface JmsProvider {

    public ConnectionFactory getConnectionFactory();

}
