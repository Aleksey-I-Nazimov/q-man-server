package org.numamo.qman.web.dto.jms;


public final class ConfigApplyRsDto {

    private String serviceId;
    private JmsRqRsConfigDto appliedConfig;

    public ConfigApplyRsDto() {
    }

    public ConfigApplyRsDto(String serviceId, JmsRqRsConfigDto appliedConfig) {
        this.serviceId = serviceId;
        this.appliedConfig = appliedConfig;
    }

    public JmsRqRsConfigDto getAppliedConfig() {
        return appliedConfig;
    }

    public void setAppliedConfig(JmsRqRsConfigDto appliedConfig) {
        this.appliedConfig = appliedConfig;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "ConfigApplyRsDto{" +
                "serviceId='" + serviceId + '\'' +
                ", appliedConfig=" + appliedConfig +
                '}';
    }
}
