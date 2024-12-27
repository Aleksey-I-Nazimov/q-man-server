package org.numamo.qman.services.api.robot;

import jakarta.jms.MessageListener;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;


public interface JmsRequestReplyRobot extends MessageListener {

    String getId();

    JmsRqRsConfigDto getConfig();

    JmsListenerEndpoint getEndpoint();

    interface Factory {

        JmsRequestReplyRobot makeFor(
                JmsTemplate jmsTemplate,
                JmsRqRsConfigDto config,
                JmsListenerEndpoint jmsListenerEndpoint
        );

    }
}
