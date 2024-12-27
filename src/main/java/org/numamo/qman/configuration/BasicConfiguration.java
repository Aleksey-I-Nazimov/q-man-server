package org.numamo.qman.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.Integer.parseInt;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class BasicConfiguration {

    private static final Logger LOGGER = getLogger(BasicConfiguration.class);

    public static final String ROBOT_THREAD_POOL_SERVICE = "ROBOT_THREAD_POOL_SERVICE";

    private int robotThreadPoolSize;

    @PostConstruct
    void init() {
        LOGGER.info("Configs {}", this);
    }

    @Value("${q-man.robot.thread-pool-size: 1}")
    public void setRobotThreadPoolSize(String robotThreadPoolSize) {
        this.robotThreadPoolSize = parseInt(robotThreadPoolSize);
    }

    @Bean(ROBOT_THREAD_POOL_SERVICE)
    public ScheduledExecutorService scheduledRobotThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(
                this.robotThreadPoolSize,
                runnable -> {
                    final Thread t = new Thread(runnable);
                    t.setPriority(Thread.MAX_PRIORITY);
                    t.setName("Robot-thread");
                    LOGGER.debug("Created the new thread {} {}", t.getName(), t.getId());
                    return t;
                });
    }

    @Override
    public String toString() {
        return "BasicConfiguration{" +
                "robotThreadPoolSize=" + robotThreadPoolSize +
                '}';
    }
}
