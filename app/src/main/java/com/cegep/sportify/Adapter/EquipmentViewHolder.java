package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify.EquipmentListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Equipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class EquipmentViewHolder extends RecyclerView.ViewHolder {

    private final ImageView equipmentImageView;
    private final TextView equipmentNameTextView;
    private final TextView equipmentPriceTextView;
    private final ImageView saleBgImageView;
    private final TextView saleTextView;
    private final TextView outOfStockOverlay;
    private Button fav_equipment_btn;

    private Equipment equipment;
    ArrayList<String> list = new ArrayList<String>();

    public EquipmentViewHolder(@NonNull View itemView, EquipmentListItemClickListener equipmentListItemClickListener) {
        super(itemView);

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
        fav_equipment_btn = itemView.findViewById(R.id.faveqbutton);
    }

    void bind(Equipment equipment, Context context) {
        this.equipment = equipment;

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
        equipmentPriceTextView.setText("$" + equipment.getPrice());

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

        fav_equipment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadList();
            }
        });
    }

    private void uploadList() {

        String equipId = equipment.getEquipmentId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference favref = databaseReference.child("Users").child(uid).child("favoriteEquipments");

        favref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (list.indexOf(equipId) == -1) {

                    if (dataSnapshot.getValue() == null) {
                        list.add(equipId);
                    } else {
                        if (dataSnapshot.getValue() instanceof List && ((List) dataSnapshot.getValue()).size() > 0 && ((List) dataSnapshot.getValue()).get(0) instanceof String) {
                            list = (ArrayList<String>) dataSnapshot.getValue();
                            list.add(equipId);
                        }
                    }
                    favref.setValue(list);
                } else {
                    list.remove(equipId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
