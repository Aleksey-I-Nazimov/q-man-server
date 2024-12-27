package org.numamo.qman.configuration.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import static java.util.Objects.isNull;

public class KafkaSerializer implements Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object map) {

        if (isNull(map)) {
            return null;
        } else {
            try {
                return new ObjectMapper().writeValueAsString(map).getBytes();
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Map cannot be serialized: "+map,e);
            }
        }
    }
}
