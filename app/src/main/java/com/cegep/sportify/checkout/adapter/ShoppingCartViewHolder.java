package com.cegep.sportify.checkout.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.sportify.R;
import com.cegep.sportify.checkout.ShoppingCartChangeListener;
import com.cegep.sportify.model.ShoppingCartItem;

public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

    private final ImageView itemImageView;
    private final TextView itemNameTextView;
    private final TextView itemPriceTextView;
    private final TextView itemSizeTextView;
    private final TextView itemQuantityTextView;
    private final TextView outOfStockOverlay;

    private final ShoppingCartChangeListener shoppingCartChangeListener;

    private ShoppingCartItem shoppingCartItem;

    public ShoppingCartViewHolder(@NonNull View itemView, ShoppingCartChangeListener shoppingCartChangeListener) {
        super(itemView);
        this.shoppingCartChangeListener = shoppingCartChangeListener;

        itemImageView = itemView.findViewById(R.id.item_image);
        itemNameTextView = itemView.findViewById(R.id.item_name);
        itemPriceTextView = itemView.findViewById(R.id.item_price);
        itemSizeTextView = itemView.findViewById(R.id.item_size);
        itemQuantityTextView = itemView.findViewById(R.id.item_quantity);
        outOfStockOverlay = itemView.findViewById(R.id.out_of_stock_overlay);

        itemView.findViewById(R.id.item_increment_quantity).setOnClickListener(v -> {
            shoppingCartItem.incrementQuantity();
            itemQuantityTextView.setText(String.valueOf(shoppingCartItem.getQuantity()));
            shoppingCartChangeListener.onShoppingCartChanged(shoppingCartItem, false);
        });

        itemView.findViewById(R.id.item_decrement_quantity).setOnClickListener(v -> {
            shoppingCartItem.decrementQuantity();
            itemQuantityTextView.setText(String.valueOf(shoppingCartItem.getQuantity()));
            shoppingCartChangeListener.onShoppingCartChanged(shoppingCartItem, false);
        });

        itemView.findViewById(R.id.delete_button).setOnClickListener(v -> {
            shoppingCartChangeListener.onShoppingCartItemDeleted(shoppingCartItem);
            shoppingCartChangeListener.onShoppingCartChanged(shoppingCartItem, true);
        });
    }

    public void bind(Context context, ShoppingCartItem shoppingCartItem) {
        this.shoppingCartItem = shoppingCartItem;

        if (!TextUtils.isEmpty(shoppingCartItem.getImage())) {
            Glide.with(context)
                    .load(shoppingCartItem.getImage())
                    .into(itemImageView);
        } else {
            itemImageView.setImageResource(R.drawable.ic_no_image);
        }

        itemNameTextView.setText(shoppingCartItem.getName());
        itemPriceTextView.setText("$" + String.format("%.2f", shoppingCartItem.getFinalPrice()));
        itemSizeTextView.setText(shoppingCartItem.getSizeString(context));
        itemQuantityTextView.setText(String.valueOf(shoppingCartItem.getQuantity()));

        if (shoppingCartItem.getProduct() != null) {
            if (shoppingCartItem.getProduct().isOutOfStock()) {
                outOfStockOverlay.setVisibility(View.VISIBLE);
            } else {
                outOfStockOverlay.setVisibility(View.GONE);
            }
        } else {
            if (shoppingCartItem.getEquipment().isOutOfStock()) {
                outOfStockOverlay.setVisibility(View.VISIBLE);
            } else {
                outOfStockOverlay.setVisibility(View.GONE);
            }
        }
    }
}
