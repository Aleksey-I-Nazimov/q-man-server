package org.numamo.qman.services.api;

import org.numamo.qman.web.dto.PostmanRsDto;

import java.time.Duration;
import java.util.Map;


/**
 * The message postman service used to send messages to different
 * topics and queues.
 *
 * @author Nazimov Aleksey I.
 */
public interface MessagePostman {

    PostmanRsDto sendMessageToKafka (
            String topicName,
            Integer partition,
            String key,
            Map<?, ?> message,
            Map<String,String> headers
    );

    PostmanRsDto receiveFromKafka(
            String topic,
            Integer partition,
            Long offset,
            Duration pollTimeout
    );

    PostmanRsDto receiveFromKafkaSubscription(
            String topic,
            String groupId,
            Integer partition,
            Duration pollTimeout
    );

    PostmanRsDto sendMessageToIbmMq(
            String queueName,
            Map<?,?> massage,
            Map<String,String> headers
    );

    PostmanRsDto receiveMessageFromIbmMq(
            String queueName,
            Duration pollTimeout,
            String messageSelector
    );

}
