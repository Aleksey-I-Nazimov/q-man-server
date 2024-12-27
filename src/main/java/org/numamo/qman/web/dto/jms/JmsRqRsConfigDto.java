package org.numamo.qman.web.dto.jms;

import org.numamo.qman.web.dto.data.DataSupplierDto;

import java.util.HashMap;
import java.util.Map;


public final class JmsRqRsConfigDto {

    private String requestQueue;
    private String responseQueue;
    private String simpleResponseTemplate;
    private Map<String, String> headerMapping = new HashMap<>();
    private DataSupplierDto dataSupply = new DataSupplierDto();

    public JmsRqRsConfigDto() {
    }

    public JmsRqRsConfigDto(
            String requestQueue,
            String responseQueue,
            String simpleResponseTemplate,
            Map<String, String> headerMapping,
            DataSupplierDto dataSupply
    ) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.simpleResponseTemplate = simpleResponseTemplate;
        this.headerMapping = headerMapping;
        this.dataSupply = dataSupply;
    }

    public String getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(String requestQueue) {
        this.requestQueue = requestQueue;
    }

    public String getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(String responseQueue) {
        this.responseQueue = responseQueue;
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
        return "JmsRqRsConfigDto{" +
                "requestQueue='" + requestQueue + '\'' +
                ", responseQueue='" + responseQueue + '\'' +
                ", simpleResponseTemplate='" + simpleResponseTemplate + '\'' +
                ", headerMapping=" + headerMapping +
                ", dataSupply=" + dataSupply +
                '}';
    }

}
