package org.numamo.qman.services.api.robot;

import org.numamo.qman.web.dto.kafka.KafkaRsConfigDto;


public interface KafkaReplyRobot extends Runnable {

    String getId();

    KafkaRsConfigDto getConfig();

    interface Factory {

        KafkaReplyRobot makeFor(
                String id,
                KafkaRsConfigDto config
        );

    }
}
