package org.numamo.qman.services.robot.generators;

import org.numamo.qman.services.api.robot.generators.DataSupplierFacade;
import org.numamo.qman.services.api.robot.generators.Generator;
import org.numamo.qman.services.api.robot.generators.GeneratorParamsDmo;
import org.numamo.qman.web.dto.data.BodySupplierDto;
import org.numamo.qman.web.dto.data.HeaderSupplierDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;


@Service
public final class DataSupplierFacadeImpl implements DataSupplierFacade {

    private static final Logger LOGGER = getLogger(DataSupplierFacadeImpl.class);

    private final Map<String, Generator> generatorMap = new HashMap<>();

    @Autowired
    public DataSupplierFacadeImpl(List<Generator> generators) {
        generatorMap.putAll(generators.stream().collect(toMap(Generator::getType, g->g)));
    }

    @Override
    public Object makeBy(
            final BodySupplierDto bodySupplier
    ) {
        LOGGER.info("Generating body data {}",bodySupplier);
        final Generator generator = getGenerator(bodySupplier.getSupplierType());
        return generator.generate(new GeneratorParamsDmo().setPossibleValues(bodySupplier.getDataRange()));
    }

    @Override
    public Object makeBy(
            final HeaderSupplierDto headerSupplier
    ) {
        LOGGER.info("Generating header data {}",headerSupplier);
        final Generator generator = getGenerator(headerSupplier.getSupplierType());
        return generator.generate(new GeneratorParamsDmo().setPossibleValues(headerSupplier.getDataRange()));
    }

    private Generator getGenerator (String type) {
        final Generator gen = generatorMap.get(type);
        if (isNull(gen)) {
            throw new IllegalArgumentException("Required generator type is not supported: "+type);
        }
        return gen;
    }
}
