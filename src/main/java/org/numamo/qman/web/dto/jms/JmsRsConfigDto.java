package org.numamo.qman.web.dto.jms;

import java.time.Duration;


public final class JmsRsConfigDto {

    private String queueName;
    private Duration pollTimeout;
    private String messageSelector;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Duration getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Duration pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public String getMessageSelector() {
        return messageSelector;
    }

    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }

    @Override
    public String toString() {
        return "JmsRsConfigDto{" +
                "queueName='" + queueName + '\'' +
                ", pollTimeout=" + pollTimeout +
                ", messageSelector='" + messageSelector + '\'' +
                '}';
    }

}
