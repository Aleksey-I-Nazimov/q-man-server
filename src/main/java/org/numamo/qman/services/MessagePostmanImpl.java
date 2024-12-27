package org.numamo.qman.services;

import jakarta.jms.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.numamo.qman.services.api.MessagePostman;
import org.numamo.qman.services.api.manual.JmsMessageCreatorFactory;
import org.numamo.qman.web.dto.PostmanRsDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.numamo.qman.configuration.jms.IbmMqConfiguration.IBM_JMS_TEMPLATE;
import static org.numamo.qman.web.dto.PostmanRsDto.error;
import static org.numamo.qman.web.dto.PostmanRsDto.ok;
import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class MessagePostmanImpl implements MessagePostman {

    private static final Logger LOGGER = getLogger(MessagePostmanImpl.class);

    private final JmsTemplate jmsTemplate;
    private final KafkaTemplate<String,Map<?,?>> kafkaTemplate;
    private final ConsumerFactory<String,Map<?,?>> consumerFactory;
    private final JmsMessageCreatorFactory messageCreatorObjectFactory;

    @Autowired
    public MessagePostmanImpl(
            @Qualifier(IBM_JMS_TEMPLATE) JmsTemplate jmsTemplate,
            KafkaTemplate<String,Map<?,?>> kafkaTemplate,
            ConsumerFactory<String,Map<?,?>> consumerFactory,
            JmsMessageCreatorFactory messageCreatorObjectFactory
    ) {
        this.jmsTemplate = jmsTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.consumerFactory = consumerFactory;
        this.messageCreatorObjectFactory = messageCreatorObjectFactory;
    }

    @Override
    public PostmanRsDto sendMessageToKafka(
            final String topicName,
            final Integer partition,
            final String key,
            final Map<?, ?> message,
            final Map<String, String> headers
    ) {
        try {
            LOGGER.info("Request for sending message: {}",message);
            final List<Header> kafkaHeaders = new ArrayList<>();
            headers.forEach((k,v)-> kafkaHeaders.add(new RecordHeader(k,v.getBytes())));

            final ProducerRecord<String,Map<?,?>> record = new ProducerRecord<>(topicName,
                    partition,System.currentTimeMillis(),key,message,kafkaHeaders);

            final var future = kafkaTemplate.send(record);
            final var result = future.get();
            LOGGER.info("Sending result: {}",result);
            return ok(record.toString());
        } catch (Exception e) {
            LOGGER.error("Kafka sender exception: cause->",e);
            return error(e);
        }
    }

    @Override
    public PostmanRsDto receiveFromKafka(
            String topic,
            Integer partition,
            Long offset,
            Duration pollTimeout
    ) {
        LOGGER.debug("Receiving: topic {}, partition {} offset {} pollTimeout {}",
                topic,partition,offset,pollTimeout);
        try {
            final ConsumerRecord<String, Map<?,?>> record =
                    kafkaTemplate.receive(topic,partition,offset,pollTimeout);
            return ok(""+record);
        } catch (Exception e) {
            LOGGER.error("Receiving from kafka error: cause->",e);
            return error(e);
        }
    }

    @Override
    public PostmanRsDto receiveFromKafkaSubscription(
            String topic,
            String groupId,
            Integer partition,
            Duration pollTimeout
    ) {
        try (final var consumer = consumerFactory.createConsumer(groupId,"q-man")){
            consumer.subscribe(List.of(topic));
            final ConsumerRecords<String,Map<?,?>> records = consumer.poll(pollTimeout);
            return ok(""+records.records(new TopicPartition(topic,partition)));

        } catch (Exception e) {
            LOGGER.error("Receiving from kafka error: cause->",e);
            return error(e);
        }
    }

    @Override
    public PostmanRsDto sendMessageToIbmMq(
            final String queueName,
            final Map<?, ?> massage,
            final Map<String, String> headers
    ) {
        try {
            final JmsMessageCreatorFactory.JmsMessageCreator creator =
                    messageCreatorObjectFactory.make(massage,headers);
            jmsTemplate.send(queueName,creator);
            return ok(creator.getLogMessage());
        } catch (Exception e) {
            LOGGER.error("IBM sending message error: cause -> ",e);
            return error(e);
        }
    }

    @Override
    public PostmanRsDto receiveMessageFromIbmMq(
            final String queueName,
            final Duration pollTimeout,
            final String messageSelector
    ) {
        LOGGER.debug("Receiving message from ibm: queue={}, timeout={}, selector={}",queueName,
                pollTimeout,messageSelector);
        try {
            jmsTemplate.setReceiveTimeout(pollTimeout.toMillis());
            final Message msg = requireNonNull(jmsTemplate.receiveSelected(queueName,messageSelector),
                    "Required message cannot be NULL: "+messageSelector);
            return ok(msg.toString()+">>>separate body string="+msg.getBody(String.class));
        } catch (Exception e) {
            LOGGER.error("Receiving ibm jms message error: cause->",e);
            return error(e);
        }
    }


}
