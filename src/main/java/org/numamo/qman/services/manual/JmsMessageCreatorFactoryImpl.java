package org.numamo.qman.services.manual;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.numamo.qman.services.api.manual.JmsMessageCreatorFactory;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class JmsMessageCreatorFactoryImpl implements JmsMessageCreatorFactory {

    private static final Logger LOGGER = getLogger(JmsMessageCreatorFactoryImpl.class);

    @Override
    public JmsMessageCreatorFactory.JmsMessageCreator make(
            Map<?, ?> messageToBeCreated,
            Map<String, String> messageHeadersToBeCreated
    ) {
        LOGGER.debug("Requesting message creator");
        return new JmsMessageCreatorFactoryImpl.StatefulTextMessageCreatorImpl(
                LOGGER,messageToBeCreated,messageHeadersToBeCreated);
    }

    public static class StatefulTextMessageCreatorImpl implements JmsMessageCreator {

        private final Logger log;
        private final Map<?,?> messageToBeCreated;
        private final Map<String,String> messageHeadersToBeCreated;
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final List<String> messageList = new CopyOnWriteArrayList<>();

        protected StatefulTextMessageCreatorImpl(
                Logger log,
                Map<?, ?> messageToBeCreated,
                Map<String, String> messageHeadersToBeCreated
        ) {
            this.log = log;
            this.messageToBeCreated = messageToBeCreated;
            this.messageHeadersToBeCreated = messageHeadersToBeCreated;
        }

        @NonNull
        @Override
        public Message createMessage(Session session) throws JMSException {
            log.debug("Creating text message for {}",messageToBeCreated);
            final TextMessage txtMsg = session.createTextMessage();
            txtMsg.setText(toJson().orElseThrow(
                    ()->new JMSException("Json message creation error")));
            for (Map.Entry<String,String> headerValue: messageHeadersToBeCreated.entrySet()) {
                txtMsg.setStringProperty(headerValue.getKey(),headerValue.getValue());
            }
            log.debug("Created txt msg = {}",txtMsg);
            this.messageList.add(txtMsg.toString());
            return txtMsg;
        }

        @Override
        public String getLogMessage () {
            try {
                return this.messageList.get(0);
            } catch (Exception e) {
                throw new IllegalStateException("The message has not already been created yet",e);
            }
        }

        private Optional<String> toJson () {
            try {
                return of(objectMapper.writeValueAsString(this.messageHeadersToBeCreated));
            } catch (JsonProcessingException e) {
                log.error("Serializing map object to json error: cause->",e);
                return empty();
            }
        }

    }

}
