/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import com.microsoft.azure.eventprocessorhost.ExceptionReceivedEventArgs;
import java.util.function.Consumer;

/**
 *
 */
public class ErrorNotificationHandler implements Consumer<ExceptionReceivedEventArgs> {

    @Override
    public void accept(ExceptionReceivedEventArgs t) {
        System.out.println("SAMPLE: Host " + t.getHostname() + " received general error notification during " + t.getAction() + ": " + t.getException().toString());
    }
}
