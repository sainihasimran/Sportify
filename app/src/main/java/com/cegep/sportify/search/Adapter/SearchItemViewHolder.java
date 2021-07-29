package com.cegep.sportify.search.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify.ItemListItemClickListner;
import com.cegep.sportify.R;
import com.cegep.sportify.model.SearchItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchItemViewHolder extends RecyclerView.ViewHolder {

    private final ImageView itemImageView;
    private final TextView itemNameTextView;
    private final TextView itemPriceTextView;

    private final ImageView saleBgImageView;
    private final TextView saleTextView;

    private final TextView outOfStockOverlay;

    private SearchItem searchItem;

    public SearchItemViewHolder(@NonNull @NotNull View itemView, ItemListItemClickListner itemListItemClickListner) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            if (searchItem != null) {
                itemListItemClickListner.onItemClicked(searchItem);
            } else {
                Toast.makeText(itemView.getContext(), "Item is null", Toast.LENGTH_SHORT).show();
            }
        });

        itemImageView = itemView.findViewById(R.id.product_image);
        itemNameTextView = itemView.findViewById(R.id.product_name);
        itemPriceTextView = itemView.findViewById(R.id.product_price);
        saleBgImageView = itemView.findViewById(R.id.sale_bg);
        saleTextView = itemView.findViewById(R.id.sale_text);
        outOfStockOverlay = itemView.findViewById(R.id.out_of_stock_overlay);
    }

    void bind(SearchItem searchItem, Context context) {
        this.searchItem = searchItem;
        boolean isOutOfStock = false;

        if (searchItem.getProduct() != null){

            if (searchItem.getProduct().getImages() != null && !searchItem.getProduct().getImages().isEmpty()) {
                Glide.with(context)
                        .load(searchItem.getProduct().getImages().get(0))
                        .centerCrop()
                        .into(itemImageView);
            } else {
                if (!searchItem.getProduct().isOutOfStock()) {
                    Glide.with(context)
                            .load(R.drawable.no_image_bg)
                            .into(itemImageView);
                } else {
                    itemImageView.setImageDrawable(null);
                }
            }

            itemNameTextView.setText(searchItem.getProduct().getProductName());

            if (searchItem.getProduct().isOnSale()) {
                itemPriceTextView.setText("$" + String.format("%.2f", searchItem.getProduct().getSalePrice()));
            } else {
                itemPriceTextView.setText("$" + String.format("%.2f", searchItem.getProduct().getPrice()));
            }

            isOutOfStock = searchItem.getProduct().isOutOfStock();

            if (searchItem.getProduct().getSale() > 0 && !isOutOfStock) {
                saleTextView.setText(searchItem.getProduct().getSale() + "%\noff");
                saleTextView.setVisibility(View.VISIBLE);
                saleBgImageView.setVisibility(View.VISIBLE);
            } else {
                saleTextView.setVisibility(View.GONE);
                saleBgImageView.setVisibility(View.GONE);
            }

        }
        else {

            if (searchItem.getEquipment().getImages() != null && !searchItem.getEquipment().getImages().isEmpty()) {
                Glide.with(context)
                        .load(searchItem.getEquipment().getImages().get(0))
                        .centerCrop()
                        .into(itemImageView);
            } else {
                if (!searchItem.getEquipment().isOutOfStock()) {
                    Glide.with(context)
                            .load(R.drawable.no_image_bg)
                            .into(itemImageView);
                } else {
                    itemImageView.setImageDrawable(null);
                }
            }

            itemNameTextView.setText(searchItem.getEquipment().getEquipmentName());

            if (searchItem.getEquipment().isOnSale()) {
                itemPriceTextView.setText("$" + String.format("%.2f", searchItem.getEquipment().getSalePrice()));
            } else {
                itemPriceTextView.setText("$" + String.format("%.2f", searchItem.getEquipment().getPrice()));
            }

            isOutOfStock = searchItem.getEquipment().isOutOfStock();
            if (searchItem.getEquipment().getSale() > 0 && !isOutOfStock) {
                saleTextView.setText(searchItem.getEquipment().getSale() + "%\noff");
                saleTextView.setVisibility(View.VISIBLE);
                saleBgImageView.setVisibility(View.VISIBLE);
            } else {
                saleTextView.setVisibility(View.GONE);
                saleBgImageView.setVisibility(View.GONE);
            }

        }

        if (isOutOfStock) {
            outOfStockOverlay.setVisibility(View.VISIBLE);
        } else {
            outOfStockOverlay.setVisibility(View.GONE);
        }

    }
}
