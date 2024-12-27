package org.numamo.qman.configuration.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Objects.isNull;

public class KafkaDeserializer implements Deserializer<Object> {

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (isNull(data)) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(data, HashMap.class);
        } catch (IOException e) {
            throw new IllegalStateException("Deserializing error: cause->",e);
        }
    }

}
