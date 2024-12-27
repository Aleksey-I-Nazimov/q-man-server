package org.numamo.qman.services.api.robot.generators;

public interface Generator {

    String getType();

    Object generate(GeneratorParamsDmo generatorParamsDmo);

}
