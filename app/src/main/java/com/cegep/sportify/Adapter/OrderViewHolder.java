package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Order;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class OrderViewHolder extends RecyclerView.ViewHolder {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

    private final TextView OrderDate;
    private final ImageView OrderImage;
    private final TextView OrderName;
    private final TextView OrderPrice;
    private final TextView OrderQuantity;
    private final TextView Status;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        OrderDate = itemView.findViewById(R.id.order_time);
        OrderImage = itemView.findViewById(R.id.OrderImage);
        OrderName = itemView.findViewById(R.id.OrderName);
        OrderPrice = itemView.findViewById(R.id.item_price);
        OrderQuantity = itemView.findViewById(R.id.Quantity);
        Status = itemView.findViewById(R.id.Status);


    }


    void bind(Order order, Context context) {

        List<String> images;
        if (order.getProduct() != null) {
            images = order.getProduct().getImages();
        } else {
            images = order.getEquipment().getImages();
        }

        if (images != null && !images.isEmpty()) {
            Glide.with(context)
                    .load(images.get(0))
                    .error(R.drawable.no_image_bg)
                    .into(OrderImage);
        } else {
            OrderImage.setImageResource(R.drawable.no_image_bg);
        }

        if (order.getProduct() != null) {
            OrderName.setText(order.getProduct().getProductName());
            OrderPrice.setText("$" + String.format("%.2f", order.getPrice()));
            OrderQuantity.setText("Quantity: " + order.getQuantity());
            Status.setText(Character.toUpperCase(order.getStatus().charAt(0)) + order.getStatus().substring(1));
        } else {
            OrderName.setText(order.getEquipment().getEquipmentName());
            OrderPrice.setText("$" + String.format("%.2f", order.getPrice()));
            OrderQuantity.setText("Quantity: " + order.getQuantity());
            Status.setText(Character.toUpperCase(order.getStatus().charAt(0)) + order.getStatus().substring(1));
        }

        if (order.getStatus().equalsIgnoreCase("pending")) {
            Status.setTextColor(itemView.getResources().getColor(R.color.buttonbg));
        } else if (order.getStatus().equalsIgnoreCase("accepted")) {
            Status.setTextColor(itemView.getResources().getColor(R.color.green));
        } else if (order.getStatus().equalsIgnoreCase("declined")) {
            Status.setTextColor(itemView.getResources().getColor(R.color.faded_red));
        }
        OrderDate.setText(sdf.format(new Date(order.getCreatedAt())));
    }

}

