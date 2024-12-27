package org.numamo.qman.web.dto.data;

import java.util.List;


public final class HeaderSupplierDto {

    private String headerName;
    private String supplierType;
    private List<String> dataRange;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
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
        return "HeaderSupplierDto{" +
                "headerName='" + headerName + '\'' +
                ", supplierType='" + supplierType + '\'' +
                ", dataRange=" + dataRange +
                '}';
    }

}
