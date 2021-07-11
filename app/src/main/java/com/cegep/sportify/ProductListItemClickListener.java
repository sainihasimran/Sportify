package com.cegep.sportify;

import com.cegep.sportify.model.Product;

public interface ProductListItemClickListener {

    void onProductClicked(Product product);

    void onFavoriteButtonClicked(Product product, boolean favorite);
}
