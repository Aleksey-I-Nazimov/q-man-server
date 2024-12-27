package org.numamo.qman.web.dto.data;

import java.util.List;


public final class BodySupplierDto {

    private String bodyPath;
    private String supplierType;
    private List<String> dataRange;

    public String getBodyPath() {
        return bodyPath;
    }

    public void setBodyPath(String bodyPath) {
        this.bodyPath = bodyPath;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public List<String> getDataRange() {
        return dataRange;
    }

    public void setDataRange(List<String> dataRange) {
        this.dataRange = dataRange;
    }

    @Override
    public String toString() {
        return "BodySupplierDto{" +
                "bodyPath='" + bodyPath + '\'' +
                ", supplierType='" + supplierType + '\'' +
                ", dataRange=" + dataRange +
                '}';
    }

}
