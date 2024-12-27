package org.numamo.qman.web.dto.data;

import java.util.ArrayList;
import java.util.List;


public final class DataSupplierDto {

    private List<BodySupplierDto> bodySuppliers= new ArrayList<>();
    private List<HeaderSupplierDto> headerSuppliers =new ArrayList<>();

    public List<BodySupplierDto> getBodySuppliers() {
        return bodySuppliers;
    }

    public void setBodySuppliers(List<BodySupplierDto> bodySuppliers) {
        this.bodySuppliers = bodySuppliers;
    }

    public List<HeaderSupplierDto> getHeaderSuppliers() {
        return headerSuppliers;
    }

    public void setHeaderSuppliers(List<HeaderSupplierDto> headerSuppliers) {
        this.headerSuppliers = headerSuppliers;
    }

    @Override
    public String toString() {
        return "DataSupplierDto{" +
                "bodySuppliers=" + bodySuppliers +
                ", headerSuppliers=" + headerSuppliers +
                '}';
    }

}
