package org.numamo.qman.services.robot.generators;

import org.numamo.qman.services.api.robot.generators.Generator;
import org.numamo.qman.services.api.robot.generators.GeneratorParamsDmo;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class GeneratorRandStrImpl implements Generator {

    private static final String RANDOM_STRING = "random_string";
    private static final String FULL_RANGE = "abcdefghijklmnopqrstuvwxyz";
    private static final Logger LOGGER = getLogger(GeneratorRandStrImpl.class);
    private static final int LENGTH = 8;

    @Override
    public String getType() {
        return RANDOM_STRING;
    }

    @Override
    public Object generate(
            final GeneratorParamsDmo generatorParams
    ) {
        final Random rand = new Random();
        final StringBuilder builder = new StringBuilder();
        for (int i=0;  i<LENGTH;  ++i) {
            builder.append(FULL_RANGE.charAt(rand.nextInt(FULL_RANGE.length())));
        }
        LOGGER.debug("Requested the random string {} by {}",builder,generatorParams);
        return builder.toString();
    }

}
