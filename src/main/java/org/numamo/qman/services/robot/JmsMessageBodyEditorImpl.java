package org.numamo.qman.services.robot;

import com.jayway.jsonpath.JsonPath;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.numamo.qman.services.api.robot.JmsMessageBodyEditor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class JmsMessageBodyEditorImpl implements JmsMessageBodyEditor {

    private static final Logger LOGGER = getLogger(JmsMessageBodyEditorImpl.class);

    @Override
    public Object getValue(
            final Message srcMessage,
            final String bodyPath
    ) {
        String prevBody=null;
        try {
            prevBody = srcMessage.getBody(String.class);
            final var value = JsonPath.parse(prevBody).read(bodyPath);
            LOGGER.debug("Read: {} from body: {}\n by path {}",value,prevBody,bodyPath);
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException("Getting body value error: "
                    +bodyPath+" body: "+prevBody);
        }

    }

    @Override
    public void setValue(
            final TextMessage targetMessage,
            final String bodyPath,
            final Object value
    ) {
        String prevBody = null;
        try {
            prevBody = targetMessage.getText();
            final String newBody = JsonPath.parse(prevBody).set(bodyPath,value).jsonString();
            targetMessage.clearBody();
            targetMessage.setText(newBody);
            LOGGER.debug("Original body: {}\nNew body: {}\nNew value: {}\nPath: {}",
                    prevBody,newBody,value,bodyPath);
        } catch (Exception e) {
            throw new IllegalArgumentException("Setting value error: "
                    +bodyPath+" value="+value+" prevBody="+prevBody);
        }
    }

}
