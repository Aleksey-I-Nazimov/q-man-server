package org.numamo.qman.services.api.robot;

import org.numamo.qman.web.dto.data.BodySupplierDto;
import org.numamo.qman.web.dto.data.HeaderSupplierDto;

import java.util.List;
import java.util.Map;

public interface KafkaMessageFactory {

    Map<?, ?> makeReplyMessage(
            String simpleResponseTemplate,
            List<BodySupplierDto> bodySuppliers
    );

    Map<String, String> makeMessageHeaders(
            Map<String, String> defaultHeaders,
            List<HeaderSupplierDto> headerSuppliers
    );

}
