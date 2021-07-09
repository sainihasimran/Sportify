package com.cegep.sportify.model;

public class ProductFilter {

    private String categoryFilter = "All";

    private String subCategoryFilter = "All";

    private Boolean outOfStock = null;

    private Boolean onSale = null;

    public String getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(String categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public String getSubCategoryFilter() {
        return subCategoryFilter;
    }

    public void setSubCategoryFilter(String subCategoryFilter) {
        this.subCategoryFilter = subCategoryFilter;
    }

    public Boolean getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }
}
