package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import java.util.List;

public class ShoppingCartItem {

    private String cartId;

    private String clientId;

    private String productId;

    private String equipmentId;

    private Product product;

    private Equipment equipment;

    private int quantity;

    private String size;

    private String color;

    private String sport;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getImage() {
        List<String> images = null;
        if (product != null) {
            images = product.getImages();
        } else if (equipment != null) {
            images = equipment.getImages();
        }

        if (images == null) {
            return null;
        }

        return images.get(0);
    }

    public String getName() {
        if (isProduct()) {
            return product.getProductName();
        } else {
            return equipment.getEquipmentName();
        }
    }

    public float getTotalPrice() {
        float price = 0f;
        if (isProduct()) {
            if (product.isOnSale()) {
                price = product.getSalePrice();
            } else {
                price = product.getPrice();
            }
        } else {
            if (equipment.isOnSale()) {
                price = equipment.getSalePrice();
            } else {
                price = equipment.getPrice();
            }
        }

        return quantity * price;
    }

    public String getSizeString(Context context) {
        if (isProduct()) {
            return context.getString(R.string.shopping_cart_size, size);
        } else {
            return context.getString(R.string.shopping_cart_sport, sport);
        }
    }

    public boolean isProduct() {
        return !TextUtils.isEmpty(productId);
    }

    public void incrementQuantity() {
        quantity += 1;
    }

    public void decrementQuantity() {
        if (quantity == 1) {
            return;
        }

        quantity -= 1;
    }

    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(Utils.getUniqueId());
        order.setCreatedAt(System.currentTimeMillis());
        order.setProduct(product);
        order.setEquipment(equipment);
        order.setQuantity(quantity);
        order.setSize(size);
        order.setColor(color);
        order.setSport(sport);
        order.setClientId(SportifyApp.user.userId);
        if (isProduct()) {
            order.setAdminId(product.getAdminId());
        } else {
            order.setAdminId(equipment.getAdminId());
        }
        order.setStatus("pending");

        return order;
    }
}
