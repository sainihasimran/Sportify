package com.cegep.sportify.checkout;

import com.cegep.sportify.model.ShoppingCartItem;

public interface ShoppingCartChangeListener {

    void onShoppingCartChanged();

    void onShoppingCartItemDeleted(ShoppingCartItem shoppingCartItem);

}
