package org.numamo.qman.services.api.robot;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;

public interface JmsMessageBodyEditor {

    Object getValue (
            Message srcMessage,
            String bodyPath
    );

    void setValue(
            TextMessage targetMessage,
            String bodyPath,
            Object value
    );

}
