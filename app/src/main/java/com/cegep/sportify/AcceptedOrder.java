package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.cegep.sportify.Adapter.OrderAdapter;


import com.cegep.sportify.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcceptedOrder extends Fragment implements OrderListItemClickListener {

    public static Order selectedOrder = null;

    private List<Order> orders = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Order> order = new ArrayList<>();
            for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                Order orders = orderDatasnapshot.getValue(Order.class);
                order.add(orders);
            }
            AcceptedOrder.this.orders = orders;
            showOrderList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    private OrderAdapter OrderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_accepted, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(view);
        FirebaseDatabase adminappdb = Utils.getClientDatabase();

        DatabaseReference ordersReference = adminappdb.getReference("Orders");
        ordersReference.addValueEventListener(valueEventListener);

    }

    private void setupRecyclerView(View view) {
        OrderAdapter = new OrderAdapter(requireContext(), this.orders, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(OrderAdapter);
    }

    private void showOrderList() {
        Set<Order> Orders = new HashSet<>();
        for (Order order : orders) {
            orders.add(order);
        }

    }

    @Override
    public void onOrderClicked(Order order) {
        selectedOrder= (Order) orders;
        Intent intent = new Intent(requireContext(), OrderActivity.class);
        startActivity(intent);
    }
}
