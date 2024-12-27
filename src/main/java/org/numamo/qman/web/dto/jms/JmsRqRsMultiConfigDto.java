package org.numamo.qman.web.dto.jms;

import org.numamo.qman.web.dto.data.DataSupplierDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JmsRqRsMultiConfigDto {

    private String requestQueueTemplate;
    private String responseQueueTemplate;
    private List<String> templateValues = new ArrayList<>();

    private String simpleResponseTemplate;
    private Map<String, String> headerMapping = new HashMap<>();
    private DataSupplierDto dataSupply = new DataSupplierDto();

    public String getRequestQueueTemplate() {
        return requestQueueTemplate;
    }

    public void setRequestQueueTemplate(String requestQueueTemplate) {
        this.requestQueueTemplate = requestQueueTemplate;
    }

    public String getResponseQueueTemplate() {
        return responseQueueTemplate;
    }

    public void setResponseQueueTemplate(String responseQueueTemplate) {
        this.responseQueueTemplate = responseQueueTemplate;
    }

    public List<String> getTemplateValues() {
        return templateValues;
    }

    public void setTemplateValues(List<String> templateValues) {
        this.templateValues = templateValues;
    }

    public String getSimpleResponseTemplate() {
        return simpleResponseTemplate;
    }

    public void setSimpleResponseTemplate(String simpleResponseTemplate) {
        this.simpleResponseTemplate = simpleResponseTemplate;
    }

    public Map<String, String> getHeaderMapping() {
        return headerMapping;
    }

    public void setHeaderMapping(Map<String, String> headerMapping) {
        this.headerMapping = headerMapping;
    }

    public DataSupplierDto getDataSupply() {
        return dataSupply;
    }

    public void setDataSupply(DataSupplierDto dataSupply) {
        this.dataSupply = dataSupply;
    }

    @Override
    public String toString() {
        return "JmsRqRsMultiConfigDto{" +
                "templateValues=" + templateValues +
                ", requestQueueTemplate='" + requestQueueTemplate + '\'' +
                ", responseQueueTemplate='" + responseQueueTemplate + '\'' +
                ", simpleResponseTemplate='" + simpleResponseTemplate + '\'' +
                ", headerMapping=" + headerMapping +
                ", dataSupply=" + dataSupply +
                '}';
    }
}
