package org.numamo.qman.web.dto.kafka;

import org.numamo.qman.web.dto.data.DataSupplierDto;

import java.util.HashMap;
import java.util.Map;


public final class KafkaRsConfigDto {

    private long rateMs=1000;
    private String topicName;
    private Integer partition;
    private String key;
    private String simpleResponseTemplate;
    private Map<String, String> defaultHeaders = new HashMap<>();
    private DataSupplierDto dataSupply = new DataSupplierDto();

    public long getRateMs() {
        return rateMs;
    }

    public void setRateMs(long rateMs) {
        this.rateMs = rateMs;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSimpleResponseTemplate() {
        return simpleResponseTemplate;
    }

    public void setSimpleResponseTemplate(String simpleResponseTemplate) {
        this.simpleResponseTemplate = simpleResponseTemplate;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public DataSupplierDto getDataSupply() {
        return dataSupply;
    }

    public void setDataSupply(DataSupplierDto dataSupply) {
        this.dataSupply = dataSupply;
    }

    @Override
    public String toString() {
        return "KafkaRsConfigDto{" +
                "rateMs=" + rateMs +
                ", topicName='" + topicName + '\'' +
                ", partition=" + partition +
                ", key='" + key + '\'' +
                ", simpleResponseTemplate='" + simpleResponseTemplate + '\'' +
                ", defaultHeaders=" + defaultHeaders +
                ", dataSupply=" + dataSupply +
                '}';
    }
}
