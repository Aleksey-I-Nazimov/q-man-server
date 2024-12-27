package org.numamo.qman.web.dto.jms;

import java.util.List;

public final class ConfigBatchRsDto {

    private List<ConfigApplyRsDto> batch;

    public ConfigBatchRsDto() {
    }

    public ConfigBatchRsDto(List<ConfigApplyRsDto> batch) {
        this.batch = batch;
    }

    public List<ConfigApplyRsDto> getBatch() {
        return batch;
    }

    public void setBatch(List<ConfigApplyRsDto> batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "ConfigBatchRsDto{" +
                "batch=" + batch +
                '}';
    }
}
