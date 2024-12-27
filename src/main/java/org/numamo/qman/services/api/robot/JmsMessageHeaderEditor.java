package org.numamo.qman.services.api.robot;

import jakarta.jms.Message;

public interface JmsMessageHeaderEditor {

    Object getValue (
            Message srcMessage,
            String sourceHeader
    );

    void setValue(
            Message targetMessage,
            String targetHeader,
            Object value
    );

}
