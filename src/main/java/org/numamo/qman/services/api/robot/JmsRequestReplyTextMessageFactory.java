package org.numamo.qman.services.api.robot;

import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;

public interface JmsRequestReplyTextMessageFactory {

    TextMessage createTxtMessage(
            Message requestMessage,
            Session responseSession,
            JmsRqRsConfigDto messagingConfig
    );

}
