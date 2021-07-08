package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.google.firebase.database.Exclude;
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

    @Exclude
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

    @Exclude
    public String getName() {
        if (isProduct()) {
            return product.getProductName();
        } else {
            return equipment.getEquipmentName();
        }
    }

    @Exclude
    public float getFinalPrice() {
        float price;
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

    @Exclude
    public String getSizeString(Context context) {
        if (isProduct()) {
            return context.getString(R.string.shopping_cart_size, size);
        } else {
            return context.getString(R.string.shopping_cart_sport, sport);
        }
    }

    @Exclude
    public boolean isProduct() {
        return !TextUtils.isEmpty(productId);
    }

    @Exclude
    public void incrementQuantity() {
        quantity += 1;
    }

    @Exclude
    public void decrementQuantity() {
        if (quantity == 1) {
            return;
        }

        quantity -= 1;
    }

    @Exclude
    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(Utils.getUniqueId());
        order.setProduct(product);
        order.setEquipment(equipment);
        order.setQuantity(quantity);
        order.setPrice(getFinalPrice());
        order.setSize(size);
        order.setColor(color);
        order.setSport(sport);
        order.setClientId(SportifyApp.user.userId);
        order.setShoppingCartItemId(cartId);
        if (isProduct()) {
            order.setAdminId(product.getAdminId());
        } else {
            order.setAdminId(equipment.getAdminId());
        }
        order.setStatus("pending");

        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShoppingCartItem that = (ShoppingCartItem) o;

        if (quantity != that.quantity) {
            return false;
        }
        if (cartId != null ? !cartId.equals(that.cartId) : that.cartId != null) {
            return false;
        }
        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) {
            return false;
        }
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) {
            return false;
        }
        if (equipmentId != null ? !equipmentId.equals(that.equipmentId) : that.equipmentId != null) {
            return false;
        }
        if (product != null ? !product.equals(that.product) : that.product != null) {
            return false;
        }
        if (equipment != null ? !equipment.equals(that.equipment) : that.equipment != null) {
            return false;
        }
        if (size != null ? !size.equals(that.size) : that.size != null) {
            return false;
        }
        if (color != null ? !color.equals(that.color) : that.color != null) {
            return false;
        }
        return sport != null ? sport.equals(that.sport) : that.sport == null;
    }

    @Override
    public int hashCode() {
        int result = cartId != null ? cartId.hashCode() : 0;
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (equipmentId != null ? equipmentId.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (equipment != null ? equipment.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (sport != null ? sport.hashCode() : 0);
        return result;
    }
}
