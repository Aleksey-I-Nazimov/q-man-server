package org.numamo.qman.services.robot.generators;

import org.numamo.qman.services.api.robot.generators.Generator;
import org.numamo.qman.services.api.robot.generators.GeneratorParamsDmo;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class GeneratorRangeStrImpl implements Generator {

    private static final Logger LOGGER = getLogger(GeneratorRangeStrImpl.class);
    private static final String RANDOM_STRING = "range_string";

    @Override
    public String getType() {
        return RANDOM_STRING;
    }

    @Override
    public Object generate(
            final GeneratorParamsDmo generatorParams
    ) {
        final Random rand = new Random();
        final List<String> strings = requireNonNull(generatorParams.getPossibleValues());
        final String result = strings.get(rand.nextInt(strings.size()));
        LOGGER.debug("Requested string {} by {}",result,generatorParams);
        return result;
    }

}
