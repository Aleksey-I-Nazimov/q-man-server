package org.numamo.qman.services.robot;

import org.numamo.qman.services.api.MessagePostman;
import org.numamo.qman.services.api.robot.KafkaMessageFactory;
import org.numamo.qman.services.api.robot.KafkaReplyRobot;
import org.numamo.qman.web.dto.kafka.KafkaRsConfigDto;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class KafkaReplyRobotFactoryImpl implements KafkaReplyRobot.Factory {

    private static final Logger LOGGER = getLogger(KafkaReplyRobotFactoryImpl.class);

    private final KafkaMessageFactory messageFactory;
    private final MessagePostman messagePostman;

    public KafkaReplyRobotFactoryImpl(
            KafkaMessageFactory messageFactory,
            MessagePostman messagePostman
    ) {
        this.messageFactory = messageFactory;
        this.messagePostman = messagePostman;
    }

    @Override
    public KafkaReplyRobot makeFor(
            final String id,
            final KafkaRsConfigDto config
    ) {
        final var robot = new KafkaReplyRobotImpl(id,config,messagePostman,messageFactory);
        LOGGER.debug("Created robot with: {}-{}",id,config);
        return robot;
    }

    public static final class KafkaReplyRobotImpl implements KafkaReplyRobot {

        private static final Logger LOGGER = getLogger(KafkaReplyRobotImpl.class);

        private final String id;
        private final KafkaRsConfigDto config;
        private final MessagePostman messagePostman;
        private final KafkaMessageFactory kafkaMessageFactory;

        public KafkaReplyRobotImpl(
                String id,
                KafkaRsConfigDto config,
                MessagePostman messagePostman,
                KafkaMessageFactory kafkaMessageFactory
        ) {
            this.id = id;
            this.config = config;
            this.messagePostman = messagePostman;
            this.kafkaMessageFactory = kafkaMessageFactory;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public KafkaRsConfigDto getConfig() {
            return config;
        }

        @Override
        public void run() {
            try {
                final Map<?, ?> message = kafkaMessageFactory.makeReplyMessage(config.getSimpleResponseTemplate(),
                        config.getDataSupply().getBodySuppliers());
                final Map<String, String> headers = kafkaMessageFactory.makeMessageHeaders(config.getDefaultHeaders(),
                        config.getDataSupply().getHeaderSuppliers());
                final var response = messagePostman.sendMessageToKafka(config.getTopicName(), config.getPartition(),
                        config.getKey(), message, headers);
                LOGGER.debug("Generated auto reply result {}", response);
            } catch (Exception e) {
                LOGGER.error("Kafka robot error: cause->",e);
            }
        }
    }
}
