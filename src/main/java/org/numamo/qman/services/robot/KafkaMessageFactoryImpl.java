package org.numamo.qman.services.robot;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.numamo.qman.services.api.robot.KafkaMessageFactory;
import org.numamo.qman.services.api.robot.generators.DataSupplierFacade;
import org.numamo.qman.web.dto.data.BodySupplierDto;
import org.numamo.qman.web.dto.data.HeaderSupplierDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class KafkaMessageFactoryImpl implements KafkaMessageFactory {

    private static final Logger LOGGER = getLogger(KafkaMessageFactoryImpl.class);

    private final DataSupplierFacade dataSupplier;

    @Autowired
    public KafkaMessageFactoryImpl(DataSupplierFacade dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    @Override
    public Map<?, ?> makeReplyMessage(
            final String simpleResponseTemplate,
            final List<BodySupplierDto> bodySuppliers
    ) {
        String response = simpleResponseTemplate;
        for (final BodySupplierDto supplier: bodySuppliers) {
            final Object value = dataSupplier.makeBy(supplier);
            response = JsonPath.parse(response).set(supplier.getBodyPath(),value).jsonString();
        }
        try {
            final Map<?, ?> responseMap = new ObjectMapper().readValue(response, HashMap.class);
            LOGGER.debug("Made response: {} from {} by {}",responseMap,simpleResponseTemplate,bodySuppliers);
            return responseMap;
        } catch (Exception e) {
            LOGGER.error("Json serialization error: cause ->",e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Map<String, String> makeMessageHeaders(
            final Map<String, String> defaultHeaders,
            final List<HeaderSupplierDto> headerSuppliers
    ) {
        final Map<String,String> headers = new HashMap<>();
        if (nonNull(defaultHeaders)) {
            headers.putAll(defaultHeaders);
        }
        for (final HeaderSupplierDto supplier: headerSuppliers) {
            final String value = dataSupplier.makeBy(supplier).toString();
            headers.put(supplier.getHeaderName(),value);
        }
        LOGGER.debug("Made headers {}, from {} by {}",headers,defaultHeaders,headerSuppliers);
        return headers;
    }

}
