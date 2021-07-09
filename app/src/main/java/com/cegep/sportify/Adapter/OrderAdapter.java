package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.Product;

import java.util.Collection;
import java.util.List;

public class OrderAdapter<OrderListItemClickListener> extends RecyclerView.Adapter<OrderViewHolder> {

    private final Context context;

    private List<Order> orders;

    private final OrderListItemClickListener OrderListItemClickListener;

    public OrderAdapter(Context context, List<Order> orders, OrderListItemClickListener OrderListItemClickListener) {
        this.context = context;
        this.orders = orders;
        this.OrderListItemClickListener = OrderListItemClickListener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recylerview_item, parent, false);
        return new OrderViewHolder(view, (com.cegep.sportify.OrderListItemClickListener) OrderListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position), context);
    }



    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void update(Collection<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        notifyDataSetChanged();
    }
}
