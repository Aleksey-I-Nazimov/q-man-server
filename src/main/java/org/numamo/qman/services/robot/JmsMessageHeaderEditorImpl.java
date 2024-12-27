package org.numamo.qman.services.robot;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.numamo.qman.services.api.robot.JmsMessageHeaderEditor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class JmsMessageHeaderEditorImpl implements JmsMessageHeaderEditor {

    private static final Logger LOGGER = getLogger(JmsMessageHeaderEditorImpl.class);

    @Override
    public Object getValue(
            final Message srcMessage,
            final String sourceHeader
    ) {
        try {
            return getJmsValue(srcMessage,sourceHeader);
        } catch (JMSException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void setValue(
            final Message targetMessage,
            final String targetHeader,
            final Object value
    ) {
        try {
            setJmsValue(targetMessage,targetHeader,value);
        } catch (JMSException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Object getJmsValue (
            final Message srcMessage,
            final String sourceHeader
    ) throws JMSException {
        if (isNull(sourceHeader)) {
            LOGGER.debug("Skipped NULL reading");
            return null;
        }
        if ("MessageID".equals(sourceHeader)) {
            return srcMessage.getJMSMessageID();
        } else if ("Timestamp".equals(sourceHeader)) {
            return srcMessage.getJMSTimestamp();
        } else if ("CorrelationID".equals(sourceHeader)) {
            return srcMessage.getJMSCorrelationID();
        } else if ("ReplyTo".equals(sourceHeader)) {
            return srcMessage.getJMSReplyTo();
        } else if ("Destination".equals(sourceHeader)) {
            return srcMessage.getJMSDestination();
        } else if ("DeliveryMode".equals(sourceHeader)) {
            return srcMessage.getJMSDeliveryMode();
        } else if ("Type".equals(sourceHeader)) {
            return srcMessage.getJMSType();
        } else if ("Expiration".equals(sourceHeader)) {
            return srcMessage.getJMSExpiration();
        } else if ("DeliveryTime".equals(sourceHeader)) {
            return srcMessage.getJMSDeliveryTime();
        } else if ("Priority".equals(sourceHeader)) {
            return srcMessage.getJMSPriority();
        } else {
            return srcMessage.propertyExists(sourceHeader) ?
                    srcMessage.getStringProperty(sourceHeader) :
                    null;
        }
    }

    private void setJmsValue(
            final Message targetMessage,
            final String targetHeader,
            final Object value
    ) throws JMSException {
        if (isNull(value) || isNull(targetHeader)) {
            LOGGER.debug("Skipped settings {} {}",value,targetHeader);
            return;
        }
        if ("MessageID".equals(targetHeader)) {
            targetMessage.setJMSMessageID((String) value);
        } else if ("Timestamp".equals(targetHeader)) {
            targetMessage.setJMSTimestamp((Long) value);
        } else if ("CorrelationID".equals(targetHeader)) {
            targetMessage.setJMSCorrelationID((String) value);
        } else if ("ReplyTo".equals(targetHeader)) {
            targetMessage.setJMSReplyTo((Destination) value);
        } else if ("Destination".equals(targetHeader)) {
            targetMessage.setJMSDestination((Destination) value);
        } else if ("DeliveryMode".equals(targetHeader)) {
            targetMessage.setJMSDeliveryMode((Integer) value);
        } else if ("Type".equals(targetHeader)) {
            targetMessage.setJMSType((String) value);
        } else if ("Expiration".equals(targetHeader)) {
            targetMessage.setJMSExpiration((Long) value);
        } else if ("DeliveryTime".equals(targetHeader)) {
            targetMessage.setJMSDeliveryTime((Long) value);
        } else if ("Priority".equals(targetHeader)) {
            targetMessage.setJMSPriority((Integer) value);
        } else {
            targetMessage.setStringProperty(targetHeader,value.toString());
        }
    }
}
