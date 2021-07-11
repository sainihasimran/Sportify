package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify.EquipmentListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Equipment;
import java.util.List;

class EquipmentViewHolder extends RecyclerView.ViewHolder {

    private final ImageView equipmentImageView;
    private final TextView equipmentNameTextView;
    private final TextView equipmentPriceTextView;
    private final ImageView saleBgImageView;
    private final TextView saleTextView;
    private final TextView outOfStockOverlay;

    private final ImageButton favoriteButton;

    private Equipment equipment;

    private final List<String> favoriteEquipments;

    private boolean favorite;

    public EquipmentViewHolder(@NonNull View itemView, EquipmentListItemClickListener equipmentListItemClickListener, List<String> favoriteEquipments) {
        super(itemView);
        this.favoriteEquipments = favoriteEquipments;

        itemView.setOnClickListener(v -> {
            if (equipment != null) {
                equipmentListItemClickListener.onEquipmentClicked(equipment);
            }
        });

        equipmentImageView = itemView.findViewById(R.id.equipment_image);
        equipmentNameTextView = itemView.findViewById(R.id.equipment_name);
        equipmentPriceTextView = itemView.findViewById(R.id.equipment_price);
        saleBgImageView = itemView.findViewById(R.id.sale_bg);
        saleTextView = itemView.findViewById(R.id.sale_text);
        outOfStockOverlay = itemView.findViewById(R.id.out_of_stock_overlay);
        favoriteButton = itemView.findViewById(R.id.favorite_button);

        favoriteButton.setOnClickListener(v -> equipmentListItemClickListener.onFavoriteButtonClicked(equipment, !favorite));
    }

    void bind(Equipment equipment, Context context) {
        this.equipment = equipment;
        this.favorite = favoriteEquipments.contains(equipment.getEquipmentId());

        if (equipment.getImages() != null && !equipment.getImages().isEmpty()) {
            Glide.with(context)
                    .load(equipment.getImages().get(0))
                    .centerCrop()
                    .into(equipmentImageView);
        } else {
            if (!equipment.isOutOfStock()) {
                Glide.with(context)
                        .load(R.drawable.no_image_bg)
                        .into(equipmentImageView);
            } else {
                equipmentImageView.setImageDrawable(null);
            }
        }

        equipmentNameTextView.setText(equipment.getEquipmentName());
        if (equipment.isOnSale()) {
            equipmentPriceTextView.setText("$" + String.format("%.2f", equipment.getSalePrice()));
        } else {
            equipmentPriceTextView.setText("$" + String.format("%.2f", equipment.getPrice()));
        }

        boolean isOutOfStock = equipment.isOutOfStock();
        if (equipment.getSale() > 0 && !isOutOfStock) {
            saleTextView.setText(equipment.getSale() + "%\noff");
            saleTextView.setVisibility(View.VISIBLE);
            saleBgImageView.setVisibility(View.VISIBLE);
        } else {
            saleTextView.setVisibility(View.GONE);
            saleBgImageView.setVisibility(View.GONE);
        }

        if (isOutOfStock) {
            outOfStockOverlay.setVisibility(View.VISIBLE);
        } else {
            outOfStockOverlay.setVisibility(View.GONE);
        }

        favoriteButton.setImageResource(favorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }
}
