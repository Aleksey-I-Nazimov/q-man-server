package org.numamo.qman.services.api.robot;

import jakarta.jms.TextMessage;
import org.numamo.qman.web.dto.data.DataSupplierDto;

public interface JmsTextMessageDataGenerator {

    void generateData(
            TextMessage targetTextMessage,
            DataSupplierDto dataSupplier
    );

}
