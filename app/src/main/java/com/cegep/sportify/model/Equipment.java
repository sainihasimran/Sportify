package com.cegep.sportify.model;


import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import java.util.ArrayList;
import java.util.List;

public class Equipment implements Comparable<Equipment> {

    private String equipmentId;

    private String equipmentName;

    private float price = -1f;

    private float salePrice;

    private int sale;

    private int stock;

    private long createdAt;

    private String sport;

    private String description;

    private String adminId;

    private List<String> images = new ArrayList<>();

    public Equipment() {

    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
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

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
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
        return getStock() <= 0;
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
        order.setEquipment(this);
        order.setClientId(SportifyApp.user.userId);
        order.setAdminId(adminId);
        order.setStatus("pending");
        order.setSport(sport);

        return order;
    }

    @Override
    public int compareTo(Equipment o) {
        return Long.compare(createdAt, o.createdAt);
    }
}
