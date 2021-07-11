package com.cegep.sportify.checkout;

import com.cegep.sportify.model.ShoppingCartItem;

public interface ShoppingCartChangeListener {

    void onShoppingCartChanged(ShoppingCartItem shoppingCartItem, boolean fromDelete);

    void onShoppingCartItemDeleted(ShoppingCartItem shoppingCartItem);
}
