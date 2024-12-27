package org.numamo.qman.web;

import org.numamo.qman.services.api.robot.ReplyAutoMessagingRegister;
import org.numamo.qman.services.api.robot.RequestReplyAutoMessagingRegister;
import org.numamo.qman.web.dto.jms.ConfigApplyRsDto;
import org.numamo.qman.web.dto.jms.ConfigBatchRsDto;
import org.numamo.qman.web.dto.jms.JmsRqRsConfigDto;
import org.numamo.qman.web.dto.jms.JmsRqRsMultiConfigDto;
import org.numamo.qman.web.dto.kafka.KafkaRsConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;


@RestController
public class AutomatedMsgWebController {

    private static final Logger LOGGER = getLogger(AutomatedMsgWebController.class);

    private final RequestReplyAutoMessagingRegister requestReplyRegister;
    private final ReplyAutoMessagingRegister replyRegister;

    @Autowired
    public AutomatedMsgWebController(
            RequestReplyAutoMessagingRegister requestReplyRegister,
            ReplyAutoMessagingRegister replyRegister
    ) {
        this.requestReplyRegister = requestReplyRegister;
        this.replyRegister = replyRegister;
    }


    @GetMapping(path = "/read/deployed-configs")
    public ResponseEntity<ConfigBatchRsDto> readDeployedConfigs() {

        LOGGER.info("Requesting all deployed configs");

        final List<ConfigApplyRsDto> responseList = new ArrayList<>();
        requestReplyRegister.deployedConfigs().forEach(
                ((k, v) -> responseList.add(new ConfigApplyRsDto(k, v))));

        return ok(new ConfigBatchRsDto(responseList));
    }


    @PostMapping(path = "/receive-reply/to/ibm-mq")
    public ResponseEntity<ConfigApplyRsDto> receiveReplyToIbmMq(
            @RequestBody JmsRqRsConfigDto jmsMessagingConfig
    ) {
        LOGGER.info("Requested message configurator: {}", jmsMessagingConfig);

        final ConfigApplyRsDto response = new ConfigApplyRsDto();
        response.setAppliedConfig(jmsMessagingConfig);
        response.setServiceId(requestReplyRegister.deployConfigsForIbmMq(jmsMessagingConfig));

        LOGGER.debug("Response message : {} ", response);
        return ok(response);
    }


    @PostMapping(path = "/multi-receive-reply/to/ibm-mq")
    public ResponseEntity<ConfigBatchRsDto> multiReceiveReplyToIbmMq(
            @RequestBody JmsRqRsMultiConfigDto jmsMessagingMultiConfig
    ) {
        LOGGER.info("Requested message multi configurator: {}", jmsMessagingMultiConfig);

        final List<ConfigApplyRsDto> responseList = new ArrayList<>();
        requestReplyRegister.deployMultiConfigsForIbmMq(jmsMessagingMultiConfig).forEach(
                ((k, v) -> responseList.add(new ConfigApplyRsDto(k, v))));

        return ok(new ConfigBatchRsDto(responseList));
    }


    @PostMapping(path = "/auto-reply/from/kafka")
    public ResponseEntity<String> autoReplyFromKafka(
            @RequestBody KafkaRsConfigDto kafkaConfig
    ) {
        LOGGER.info("Requested auto reply from kafka: {}", kafkaConfig);
        return ok(replyRegister.deployConfigsForKafka(kafkaConfig));
    }

}
