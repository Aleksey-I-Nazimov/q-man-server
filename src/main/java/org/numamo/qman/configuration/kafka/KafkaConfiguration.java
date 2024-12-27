package org.numamo.qman.configuration.kafka;


import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


/*
* https://github.com/spring-projects/spring-kafka/blob/main/spring-kafka/src/main/java/org/springframework/kafka/core/KafkaTemplate.java
*/
@EnableKafka
@Configuration
public class KafkaConfiguration {

    private static final Logger LOGGER = getLogger(KafkaConfiguration.class);

    private String kafkaBroker;

    @Value("${kafka.broker}")
    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker=kafkaBroker;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Configurations: {}",this);
    }

    @Bean
    public ProducerFactory<String, Map<?,?>> producerFactory() {

        final var props = new HashMap<String, Object>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBroker);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,KafkaSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String,Map<?,?>> consumerFactory () {
        final var props = new HashMap<String, Object>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBroker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,KafkaDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Map<?,?>> kafkaTemplate(
            ConsumerFactory<String, Map<?,?>> consumerFactory,
            ProducerFactory<String, Map<?,?>> producerFactory
    ) {
        final KafkaTemplate<String, Map<?,?>> tmp = new KafkaTemplate<>(producerFactory);
        tmp.setConsumerFactory(consumerFactory);
        return tmp;
    }

    @Override
    public String toString() {
        return "KafkaConfiguration{" +
                "kafkaBroker='" + kafkaBroker + '\'' +
                '}';
    }
}
