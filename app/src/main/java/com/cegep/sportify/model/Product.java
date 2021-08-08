package com.cegep.sportify.model;

import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import java.util.ArrayList;
import java.util.List;

public class Product implements Comparable<Product> {

    private String productId;

    private String productName;

    private float price = -1f;

    private float salePrice;

    private String sport;

    private String team;

    private int sale;

    private String category;

    private String subCategory;

    private String description;

    private String adminId;

    private int xSmallSize = 0;

    private int smallSize = 0;

    private int mediumSize = 0;

    private int largeSize = 0;

    private int xLargeSize = 0;

    private long createdAt;

    private List<String> colors = new ArrayList<>();

    private List<String> images = new ArrayList<>();

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getxSmallSize() {
        return xSmallSize;
    }

    public void setxSmallSize(int xSmallSize) {
        this.xSmallSize = xSmallSize;
    }

    public int getSmallSize() {
        return smallSize;
    }

    public void setSmallSize(int smallSize) {
        this.smallSize = smallSize;
    }

    public int getMediumSize() {
        return mediumSize;
    }

    public void setMediumSize(int mediumSize) {
        this.mediumSize = mediumSize;
    }

    public int getLargeSize() {
        return largeSize;
    }

    public void setLargeSize(int largeSize) {
        this.largeSize = largeSize;
    }

    public int getxLargeSize() {
        return xLargeSize;
    }

    public void setxLargeSize(int xLargeSize) {
        this.xLargeSize = xLargeSize;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isOnSale() {
        return sale > 0;
    }

    public boolean isOutOfStock() {
        return (xSmallSize + smallSize + mediumSize + largeSize + xLargeSize) <= 0;
    }

    public boolean hasXSmallSize() {
        return xSmallSize > 0;
    }

    public boolean hasSmallSize() {
        return smallSize > 0;
    }

    public boolean hasMediumSize() {
        return mediumSize > 0;
    }

    public boolean hasLargeSize() {
        return largeSize > 0;
    }

    public boolean hasXLargeSize() {
        return xLargeSize > 0;
    }

    public boolean hasColors() {
        return !colors.isEmpty();
    }

    public float getFinalPrice() {
        float price = this.price;
        if (isOnSale()) {
            price = getSalePrice();
        }

        return price;
    }

    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(Utils.getUniqueId());
        order.setProduct(this);
        order.setClientId(SportifyApp.user.userId);
        order.setAdminId(adminId);
        order.setStatus("pending");

        return order;
    }

    @Override
    public int compareTo(Product o) {
        return Long.compare(createdAt, o.createdAt);
    }
}
