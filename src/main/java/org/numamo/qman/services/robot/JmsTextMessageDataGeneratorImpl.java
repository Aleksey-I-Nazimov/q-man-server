package org.numamo.qman.services.robot;

import jakarta.jms.TextMessage;
import org.numamo.qman.services.api.robot.JmsMessageBodyEditor;
import org.numamo.qman.services.api.robot.JmsMessageHeaderEditor;
import org.numamo.qman.services.api.robot.JmsTextMessageDataGenerator;
import org.numamo.qman.services.api.robot.generators.DataSupplierFacade;
import org.numamo.qman.web.dto.data.BodySupplierDto;
import org.numamo.qman.web.dto.data.DataSupplierDto;
import org.numamo.qman.web.dto.data.HeaderSupplierDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class JmsTextMessageDataGeneratorImpl implements JmsTextMessageDataGenerator {

    private static final Logger LOGGER = getLogger(JmsTextMessageDataGeneratorImpl.class);

    private final DataSupplierFacade supplierFacade;
    private final JmsMessageHeaderEditor headerEditor;
    private final JmsMessageBodyEditor jmsMessageBodyEditor;

    @Autowired
    public JmsTextMessageDataGeneratorImpl(
            DataSupplierFacade supplierFacade,
            JmsMessageHeaderEditor headerEditor,
            JmsMessageBodyEditor jmsMessageBodyEditor
    ) {
        this.supplierFacade = supplierFacade;
        this.headerEditor = headerEditor;
        this.jmsMessageBodyEditor = jmsMessageBodyEditor;
    }

    @Override
    public void generateData(
            final TextMessage targetTextMessage,
            final DataSupplierDto dataSupplier
    ) {
        LOGGER.debug("Generating new data by {}",dataSupplier);
        applySuppliers(
                targetTextMessage,
                nonNull(dataSupplier.getHeaderSuppliers()) ? dataSupplier.getHeaderSuppliers(): emptyList(),
                nonNull(dataSupplier.getBodySuppliers()) ? dataSupplier.getBodySuppliers(): emptyList()
        );
    }

    private void applySuppliers(
            final TextMessage textMessage,
            final List<HeaderSupplierDto> headerSuppliers,
            final List<BodySupplierDto> bodySuppliers
    ) {
        headerSuppliers.forEach(s->applyHeaderSupplier(textMessage,s));
        bodySuppliers.forEach(s->applyBodySupplier(textMessage,s));
    }

    private void applyHeaderSupplier(
            final TextMessage textMessage,
            final HeaderSupplierDto headerSupplier
    ) {
        final String value = (String) supplierFacade.makeBy(headerSupplier);
        headerEditor.setValue(textMessage,headerSupplier.getHeaderName(),value);
    }

    private void applyBodySupplier(
            final TextMessage textMessage,
            final BodySupplierDto bodySupplierDto
    ) {
        final String value = (String) supplierFacade.makeBy(bodySupplierDto);
        jmsMessageBodyEditor.setValue(textMessage,bodySupplierDto.getBodyPath(),value);
    }

}
