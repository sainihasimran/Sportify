package com.cegep.sportify.model;

public class EquipmentFilter {

    private String sportFilter = "All";

    private Boolean outOfStock = null;

    public String getSportFilter() {
        return sportFilter;
    }

    public void setSportFilter(String sportFilter) {
        this.sportFilter = sportFilter;
    }

    public Boolean getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
}
