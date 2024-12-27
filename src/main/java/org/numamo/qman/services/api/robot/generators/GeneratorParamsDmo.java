package org.numamo.qman.services.api.robot.generators;

import java.util.List;

public final class GeneratorParamsDmo {

    private List<String> possibleValues;

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public GeneratorParamsDmo setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
        return this;
    }

    @Override
    public String toString() {
        return "GeneratorParamsDmo{" +
                "possibleValues=" + possibleValues +
                '}';
    }
}
