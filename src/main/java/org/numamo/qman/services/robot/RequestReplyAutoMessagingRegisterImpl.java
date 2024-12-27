package org.numamo.qman.services.robot;

import org.numamo.qman.services.api.BasicService;
import org.numamo.qman.services.api.robot.JmsRequestReplyRobot;
import org.numamo.qman.services.api.robot.RequestReplyAutoMessagingRegister;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.numamo.qman.web.dto.jms.JmsRqRsMultiConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.numamo.qman.configuration.jms.IbmMqConfiguration.IBM_JMS_LISTENER_ENDPOINT_REGISTRAR;
import static org.numamo.qman.configuration.jms.IbmMqConfiguration.IBM_JMS_TEMPLATE;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class RequestReplyAutoMessagingRegisterImpl implements RequestReplyAutoMessagingRegister {

    private static final Logger LOGGER = getLogger(RequestReplyAutoMessagingRegisterImpl.class);

    private final BasicService basicService;
    private final JmsTemplate ibmJmsTemplate;
    private final JmsRequestReplyRobot.Factory requestReplyMsgFactory;
    private final JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar;
    private final Map<String, JmsRequestReplyRobot> messengerStore = new ConcurrentHashMap<>();

    @Autowired
    public RequestReplyAutoMessagingRegisterImpl(
            BasicService basicService,
            @Qualifier(IBM_JMS_TEMPLATE)
            JmsTemplate ibmJmsTemplate,
            JmsRequestReplyRobot.Factory requestReplyMsgFactory,
            @Qualifier(IBM_JMS_LISTENER_ENDPOINT_REGISTRAR)
            JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar
    ) {
        this.basicService = basicService;
        this.ibmJmsTemplate = ibmJmsTemplate;
        this.requestReplyMsgFactory = requestReplyMsgFactory;
        this.jmsListenerEndpointRegistrar = jmsListenerEndpointRegistrar;
    }

    @Override
    public String deployConfigsForIbmMq(
            final JmsRqRsConfigDto config
    ) {
        final String id = basicService.makeId();
        final SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        final JmsRequestReplyRobot messenger = requestReplyMsgFactory.makeFor(ibmJmsTemplate,config,endpoint);
        endpoint.setMessageListener(messenger);
        endpoint.setDestination(config.getRequestQueue());
        endpoint.setId(id);

        jmsListenerEndpointRegistrar.registerEndpoint(endpoint);
        messengerStore.put(id,messenger);

        LOGGER.info("Request reply messenger was created with: {}",id);
        return id;
    }

    @Override
    public Map<String, JmsRqRsConfigDto> deployMultiConfigsForIbmMq(
            final JmsRqRsMultiConfigDto multiConfig
    ) {
        LOGGER.debug("Processing multi config size={}",multiConfig.getTemplateValues().size());

        final Map<String, JmsRqRsConfigDto> deployedMap = new HashMap<>();
        for (final String templateValue : multiConfig.getTemplateValues()) {
            final JmsRqRsConfigDto request = new JmsRqRsConfigDto(
                    fillTemplate(multiConfig.getRequestQueueTemplate(),templateValue),
                    fillTemplate(multiConfig.getResponseQueueTemplate(),templateValue),
                    multiConfig.getSimpleResponseTemplate(),
                    multiConfig.getHeaderMapping(),
                    multiConfig.getDataSupply()
            );
            final String deployedId = deployConfigsForIbmMq(request);
            deployedMap.put(deployedId,request);
        }
        LOGGER.debug("Deployed configs: {}",deployedMap);
        return deployedMap;
    }

    @Override
    public Map<String, JmsRqRsConfigDto> deployedConfigs() {
        final Map<String, JmsRqRsConfigDto> configs = new HashMap<>();
        messengerStore.forEach(((k,v)->configs.put(k,v.getConfig())));
        LOGGER.debug("Requested configs: {}",configs);
        return configs;
    }

    private String fillTemplate (
            final String template,
            final String value
    ) {
        final String finalValue;
        if (template.contains("%s")) {
            finalValue= template.formatted(value);
        } else {
            finalValue= template+value;
        }
        LOGGER.debug("Filled {} from {} by {}",finalValue,template,value);
        return finalValue;
    }


}
