package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify.ProductListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    ImageView productImageView;
    private final TextView productNameTextView;
    private final TextView productPriceTextView;

    private final ImageView saleBgImageView;
    private final TextView saleTextView;

    private final TextView outOfStockOverlay;

    private Button fav_product_btn;

    private DatabaseReference databaseReference;

    private Product product;
    ArrayList<String> value = new ArrayList<String>();

    public ProductViewHolder(@NonNull View itemView, ProductListItemClickListener productListItemClickListener) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            if (product != null) {
                productListItemClickListener.onProductClicked(product);
            } else {
                Toast.makeText(itemView.getContext(), "Product is null", Toast.LENGTH_SHORT).show();
            }
        });

        productImageView = itemView.findViewById(R.id.product_image);
        productNameTextView = itemView.findViewById(R.id.product_name);
        productPriceTextView = itemView.findViewById(R.id.product_price);
        saleBgImageView = itemView.findViewById(R.id.sale_bg);
        saleTextView = itemView.findViewById(R.id.sale_text);
        outOfStockOverlay = itemView.findViewById(R.id.out_of_stock_overlay);
        fav_product_btn = itemView.findViewById(R.id.fav_product_btn);
    }

    void bind(Product product, Context context) {
        this.product = product;

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Glide.with(context)
                    .load(product.getImages().get(0))
                    .centerCrop()
                    .into(productImageView);
        } else {
            if (!product.isOutOfStock()) {
                Glide.with(context)
                        .load(R.drawable.no_image_bg)
                        .into(productImageView);
            } else {
                productImageView.setImageDrawable(null);
            }
        }

        productNameTextView.setText(product.getProductName());
        if (product.isOnSale()) {
            productPriceTextView.setText("$" + String.format("%.2f", product.getSalePrice()));
        } else {
            productPriceTextView.setText("$" + String.format("%.2f", product.getPrice()));
        }

        boolean isOutOfStock = product.isOutOfStock();
        if (product.getSale() > 0 && !isOutOfStock) {
            saleTextView.setText(product.getSale() + "%\noff");
            saleTextView.setVisibility(View.VISIBLE);
            saleBgImageView.setVisibility(View.VISIBLE);
        } else {
            saleTextView.setVisibility(View.GONE);
            saleBgImageView.setVisibility(View.GONE);
        }

        if (isOutOfStock) {
            outOfStockOverlay.setVisibility(View.VISIBLE);
            fav_product_btn.setEnabled(false);
        } else {
            outOfStockOverlay.setVisibility(View.GONE);
        }

        fav_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadList();
            }
        });
    }

    private void uploadList() {

       String pId = product.getProductId();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference favref = databaseReference.child("Users").child(uid).child("favoriteProducts");
        //.push();
        //favref.setValue(pId);

        favref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(value.indexOf(pId) == -1 ) {
                    fav_product_btn.setBackgroundResource(R.drawable.saveproducts);
                    if (dataSnapshot.getValue() == null) {
                        value.add(pId);
                    } else {
                        if (dataSnapshot.getValue() instanceof List && ((List) dataSnapshot.getValue()).size() > 0 && ((List) dataSnapshot.getValue()).get(0) instanceof String) {
                            value = (ArrayList<String>) dataSnapshot.getValue();
                            value.add(pId);
                        }
                    }
                    favref.setValue(value);
                }
                else{
                    value.remove(pId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            }
    }

