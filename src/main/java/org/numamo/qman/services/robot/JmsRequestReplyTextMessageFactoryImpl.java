package org.numamo.qman.services.robot;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.numamo.qman.services.api.robot.JmsMessageHeaderEditor;
import org.numamo.qman.services.api.robot.JmsRequestReplyTextMessageFactory;
import org.numamo.qman.services.api.robot.JmsTextMessageDataGenerator;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class JmsRequestReplyTextMessageFactoryImpl implements JmsRequestReplyTextMessageFactory {

    private static final Logger LOGGER = getLogger(JmsRequestReplyTextMessageFactoryImpl.class);

    private final JmsMessageHeaderEditor headerEditor;
    private final JmsTextMessageDataGenerator dataGenerator;

    @Autowired
    public JmsRequestReplyTextMessageFactoryImpl(
            JmsMessageHeaderEditor headerEditor,
            JmsTextMessageDataGenerator dataGenerator
    ) {
        this.headerEditor = headerEditor;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public TextMessage createTxtMessage(
            final Message requestMessage,
            final Session responseSession,
            final JmsRqRsConfigDto messagingConfig
    ) {
        try {
            final String msg = requestMessage.getBody(String.class);
            LOGGER.info("Received message: {} {}",requestMessage.getJMSMessageID(),msg);
            final TextMessage txt = responseSession.createTextMessage();
            txt.setText(messagingConfig.getSimpleResponseTemplate());
            LOGGER.debug("Generated response: {}",messagingConfig.getSimpleResponseTemplate());
            processHeaders(txt,requestMessage,messagingConfig.getHeaderMapping());
            dataGenerator.generateData(txt,messagingConfig.getDataSupply());
            return txt;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private void processHeaders (
            TextMessage targetMessage,
            Message srcMessage,
            Map<String,String> headersMap
    ) {
        for (final Map.Entry<String,String> entry : headersMap.entrySet()) {
            processHeader(targetMessage,srcMessage,entry.getKey(),entry.getValue());
        }
    }

    private void processHeader(
            final TextMessage targetMessage,
            final Message sourceMessage,
            final String targetHeader,
            final String sourceHeader
    ) {
        if (isNull(targetHeader) || isNull(sourceHeader)) {
            LOGGER.warn("Skipped processing: {}->{}",targetHeader,sourceHeader);
            return;
        }
        try {
            final Object value = headerEditor.getValue(sourceMessage, sourceHeader);
            headerEditor.setValue(targetMessage, targetHeader, value);
            LOGGER.debug("Read header {} written {} with value: {}",
                    sourceHeader,targetHeader,value);
        } catch (Exception e) {
            LOGGER.error("Failed to read from "+sourceHeader+" and write to "+targetHeader,e);
        }
    }

}
