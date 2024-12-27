package org.numamo.qman.services.robot;

import jakarta.jms.Message;
import org.numamo.qman.services.api.robot.JmsRequestReplyRobot;
import org.numamo.qman.services.api.robot.JmsRequestReplyTextMessageFactory;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class RequestReplyRobotFactoryImpl implements JmsRequestReplyRobot.Factory {

    private static final Logger LOGGER = getLogger(RequestReplyRobotFactoryImpl.class);

    private final JmsRequestReplyTextMessageFactory messageProcessor;

    @Autowired
    public RequestReplyRobotFactoryImpl(
            JmsRequestReplyTextMessageFactory messageProcessor
    ) {
        this.messageProcessor = messageProcessor;
    }

    @Override
    public JmsRequestReplyRobot makeFor(
            JmsTemplate jmsTemplate,
            JmsRqRsConfigDto config,
            JmsListenerEndpoint jmsListenerEndpoint
    ) {
        LOGGER.debug("Creating rq-rp with {} {}", jmsListenerEndpoint, config);
        return new JmsRequestReplyRobotImpl(jmsTemplate, config, jmsListenerEndpoint,
                messageProcessor);
    }

    static class JmsRequestReplyRobotImpl implements JmsRequestReplyRobot {

        private final JmsTemplate jmsTemplate;
        private final JmsRqRsConfigDto config;
        private final JmsListenerEndpoint jmsListenerEndpoint;
        private final JmsRequestReplyTextMessageFactory messageProcessor;

        public JmsRequestReplyRobotImpl(
                JmsTemplate jmsTemplate,
                JmsRqRsConfigDto config,
                JmsListenerEndpoint jmsListenerEndpoint,
                JmsRequestReplyTextMessageFactory messageProcessor
        ) {
            this.jmsTemplate = jmsTemplate;
            this.config = config;
            this.jmsListenerEndpoint = jmsListenerEndpoint;
            this.messageProcessor = messageProcessor;
        }

        @Override
        public String getId() {
            return getEndpoint().getId();
        }

        @Override
        public JmsRqRsConfigDto getConfig() {
            return config;
        }

        @Override
        public JmsListenerEndpoint getEndpoint() {
            return jmsListenerEndpoint;
        }

        @Override
        public void onMessage(Message message) {
            jmsTemplate.send(
                    config.getResponseQueue(),
                    session -> messageProcessor.createTxtMessage(message,session,config)
            );
        }

    }
}
