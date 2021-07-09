package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.sportify.OrderListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Order;

class OrderViewHolder extends RecyclerView.ViewHolder {

    private final ImageView OrderImage;
    private final TextView OrderName;
    private final TextView OrderPrice;


    private Order order;

    public OrderViewHolder(@NonNull View itemView, OrderListItemClickListener orderListItemClickListener) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            if (order != null) {
                orderListItemClickListener.onOrderClicked(order);
            } else {
                Toast.makeText(itemView.getContext(), "order is null", Toast.LENGTH_SHORT).show();
            }
        });

        OrderImage = itemView.findViewById(R.id.OrderImage);
        OrderName = itemView.findViewById(R.id.OrderName);
        OrderPrice = itemView.findViewById(R.id.OrderPrice);

        OrderName.setText(order.getOrderId());
        OrderPrice.setText("$" + order.getPrice());

    }


    void bind(Order order, Context context) {
        this.order = order;

        if (order.getImages() != null && !order.getImages().isEmpty()) {
            Glide.with(context)
                    .load(order.getImages().get(0))
                    /*.centerCrop()*/
                    .into(OrderImage);
        }  else {
                OrderImage.setImageDrawable(null);
            }
        }
    }

