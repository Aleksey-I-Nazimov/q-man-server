package org.numamo.qman.services.robot;

import org.numamo.qman.services.api.BasicService;
import org.numamo.qman.services.api.robot.KafkaReplyRobot;
import org.numamo.qman.services.api.robot.ReplyAutoMessagingRegister;
import org.numamo.qman.web.dto.kafka.KafkaRsConfigDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.numamo.qman.configuration.BasicConfiguration.ROBOT_THREAD_POOL_SERVICE;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public final class ReplyAutoMessagingRegisterImpl implements ReplyAutoMessagingRegister {

    private static final Logger LOGGER = getLogger(ReplyAutoMessagingRegisterImpl.class);

    private final BasicService basicService;
    private final KafkaReplyRobot.Factory factory;
    private final ScheduledExecutorService executorService;
    private final Map<String,KafkaReplyRobot> kafkaRobots = new HashMap<>();

    public ReplyAutoMessagingRegisterImpl(
            BasicService basicService,
            KafkaReplyRobot.Factory factory,
            @Qualifier(ROBOT_THREAD_POOL_SERVICE)
            ScheduledExecutorService executorService
    ) {
        this.factory = factory;
        this.basicService = basicService;
        this.executorService = executorService;
    }


    @Override
    public String deployConfigsForKafka(
            final KafkaRsConfigDto config
    ) {
        final String id = basicService.makeId();
        final KafkaReplyRobot robot = factory.makeFor(id,config);
        executorService.scheduleAtFixedRate(robot,config.getRateMs(),
                config.getRateMs(), TimeUnit.MILLISECONDS);
        kafkaRobots.put(id,robot);
        LOGGER.debug("Kafka robot {} reply was deployed for {}",id,config);
        return id;
    }

}
