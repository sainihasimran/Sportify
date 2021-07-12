package com.cegep.sportify.model;

public class ProductFilter {

    private String categoryFilter = "All";

    private String subCategoryFilter = "All";

    private String brandFilter = "All";

    private Boolean outOfStock = null;

    private Boolean onSale = null;

    private Boolean favorite = null;

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

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
