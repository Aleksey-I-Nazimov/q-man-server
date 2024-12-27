package org.numamo.qman.web;

import org.numamo.qman.services.api.MessagePostman;
import org.numamo.qman.services.api.manual.HeadersFilter;
import org.numamo.qman.web.dto.PostmanRsDto;
import org.numamo.qman.web.dto.jms.JmsRsConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

import static java.lang.Integer.valueOf;
import static java.util.Objects.nonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;


@RestController
public class ManualMsgWebController {

    private static final Logger LOGGER = getLogger(ManualMsgWebController.class);

    private final MessagePostman messagePostman;
    private final HeadersFilter headersFilter;

    @Autowired
    public ManualMsgWebController(
            MessagePostman messagePostman,
            HeadersFilter headersFilter
    ) {
        this.messagePostman = messagePostman;
        this.headersFilter = headersFilter;
    }

    @PostMapping(path = "/send/to/kafka")
    public ResponseEntity<PostmanRsDto> sendToKafka(
            @RequestBody Map<String, Object> sentMessage,
            @RequestParam String topic,
            @RequestParam(required = false) String partition,
            @RequestParam(required = false) String key,
            @RequestHeader(required = false) Map<String,String> requestHeaders
    ) {
        LOGGER.info("Requested message for kafka sending {}",sentMessage);
        final var response = messagePostman.sendMessageToKafka(
                topic,
                nonNull(partition) ? valueOf(partition): null,
                key,
                sentMessage,
                headersFilter.makeForKafka(requestHeaders)
        );
        return makeRs(response);
    }

    @PostMapping(path = "/receive/from/kafka")
    public ResponseEntity<PostmanRsDto> receiveFromKafka(
            @RequestParam String topic,
            @RequestParam(required = false) String partition,
            @RequestParam(required = false) String offset,
            @RequestParam(required = false) String pollTimeSec
    ) {
        LOGGER.info("Receiving message from kafka: {}",topic);
        final var response = messagePostman.receiveFromKafka(
                topic,
                nonNull(partition)? Integer.parseInt(partition):0,
                nonNull(offset)? Long.parseLong(offset):0,
                nonNull(pollTimeSec)?Duration.ofSeconds(Long.parseLong(pollTimeSec)): Duration.ofSeconds(0)
        );
        return makeRs(response);
    }

    @PostMapping(path = "/subscribe/to/kafka")
    public ResponseEntity<PostmanRsDto> subscribeToKafka(
            @RequestParam String topic,
            @RequestParam String groupId,
            @RequestParam(required = false) String partition,
            @RequestParam(required = false) String pollTimeSec
    ) {
        LOGGER.info("Subscribing to message from kafka: {}",topic);
        final var response = messagePostman.receiveFromKafkaSubscription(
                topic,
                groupId,
                nonNull(partition)? Integer.parseInt(partition):0,
                nonNull(pollTimeSec)?Duration.ofSeconds(Long.parseLong(pollTimeSec)): Duration.ofSeconds(1)
        );
        return makeRs(response);
    }

    @PostMapping(path = "/send/to/ibm-mq")
    public ResponseEntity<PostmanRsDto> sendToIbmMq(
            @RequestBody Map<String, Object> sentMessage,
            @RequestParam String queue,
            @RequestHeader(required = false) Map<String,String> requestHeaders
    ) {
        LOGGER.info("Requested message for ibm sending {}",sentMessage);
        final var response = messagePostman.sendMessageToIbmMq(
                queue,
                sentMessage,
                headersFilter.makeForIbm(requestHeaders)
        );
        return makeRs(response);
    }

    @PostMapping(path = "/receive/from/ibm-mq")
    public ResponseEntity<PostmanRsDto> receiveFromIbmMq(
            @RequestBody JmsRsConfigDto responseConfig
    ) {
        LOGGER.info("Response config: {}",responseConfig);
        final var response = messagePostman.receiveMessageFromIbmMq(
                responseConfig.getQueueName(),
                responseConfig.getPollTimeout(),
                responseConfig.getMessageSelector()
        );
        return makeRs(response);
    }


    private ResponseEntity<PostmanRsDto> makeRs (
            final PostmanRsDto response
    ) {
        if (response.isOk()) {
            return ok(response);
        } else {
            return status(INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
