/*
 */
package com.cleverfishsoftware.eventhub.consumer;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

/**
 *
 */
public class EventProcessor implements IEventProcessor {

    private int checkpointBatchingCount = 0;

    @Override
    public void onOpen(PartitionContext context) throws Exception {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " is opening");
    }

    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " is closing for reason " + reason.toString());
    }

    @Override
    public void onError(PartitionContext context, Throwable error) {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " onError: " + error.toString());
    }

    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> messages) throws Exception {
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " got message batch");
        int messageCount = 0;
        for (EventData data : messages) {
            System.out.println("SAMPLE (" + context.getPartitionId() + "," + data.getSystemProperties().getOffset() + ","
                    + data.getSystemProperties().getSequenceNumber() + "): " + new String(data.getBytes(), "UTF8"));
            messageCount++;

            this.checkpointBatchingCount++;
            if ((checkpointBatchingCount % 5) == 0) {
                System.out.println("SAMPLE: Partition " + context.getPartitionId() + " checkpointing at "
                        + data.getSystemProperties().getOffset() + "," + data.getSystemProperties().getSequenceNumber());
                context.checkpoint(data);
            }
        }
        System.out.println("SAMPLE: Partition " + context.getPartitionId() + " batch size was " + messageCount + " for host " + context.getOwner());
    }

}
