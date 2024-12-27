package org.numamo.qman.services.api.robot;

import org.numamo.qman.web.dto.kafka.KafkaRsConfigDto;

public interface ReplyAutoMessagingRegister {

    String deployConfigsForKafka(KafkaRsConfigDto config);

}
