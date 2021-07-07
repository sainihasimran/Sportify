package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cegep.sportify.R;

import java.util.ArrayList;
import java.util.List;

public class Equipment {

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

    public Equipment(Equipment other) {
        this.equipmentId = other.equipmentId;
        this.equipmentName = other.equipmentName;
        this.price = other.price;
        this.sale = other.sale;
        this.stock = other.stock;
        this.createdAt = other.createdAt;
        this.sport = other.sport;
        this.description = other.description;
        this.images = other.images;
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
        this.equipmentName = equipmentName.trim();
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

    public String getSport() {
        return sport;
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

    public void setSport(String sport) {
        this.sport = sport.trim();
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

    public void setEquipmentPrice(String priceStr) {
        try {
            setPrice(Float.parseFloat(priceStr));
        }
        catch (NumberFormatException e) {
        }
    }

    public boolean isOutOfStock() {
        return getStock() <= 0;
    }

    public boolean isOnSale() {
        return sale > 0;
    }
}
