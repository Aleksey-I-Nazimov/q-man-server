package org.numamo.qman.services.api.manual;

import org.springframework.jms.core.MessageCreator;

import java.util.Map;

public interface JmsMessageCreatorFactory {

    JmsMessageCreatorFactory.JmsMessageCreator make(
            Map<?, ?> messageToBeCreated,
            Map<String, String> messageHeadersToBeCreated
    );

    interface JmsMessageCreator extends MessageCreator {

        String getLogMessage ();

    }

}
