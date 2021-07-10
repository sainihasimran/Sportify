package com.cegep.sportify.model;

public class EquipmentFilter {

    private String sportFilter = "All";

    private String brandFilter = "All";

    private Boolean outOfStock = null;

    private Boolean onSale = null;

    public String getSportFilter() {
        return sportFilter;
    }

    public void setSportFilter(String sportFilter) {
        this.sportFilter = sportFilter;
    }

    public String getBrandFilter() {
        return brandFilter;
    }

    public void setBrandFilter(String brandFilter) {
        this.brandFilter = brandFilter;
    }

    public Boolean getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Boolean getOnSale() { return onSale; }

    public void setOnSale(Boolean onSale) { this.onSale = onSale; }
}
