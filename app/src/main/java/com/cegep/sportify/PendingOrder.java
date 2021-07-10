package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PendingOrder extends Fragment {



    private OrderAdapter orderAdapter;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Order> orders = new ArrayList<>();
            for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                Order order = orderDatasnapshot.getValue(Order.class);
                if (order != null && "pending".equals(order.getStatus())) {
                    orders.add(order);
                }
            }
            if (orders.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            orderAdapter.update(orders);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    private TextView emptyView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView = view.findViewById(R.id.empty_view);


        setupRecyclerView(view);

        Query query = Utils.getClientDatabase().getReference("Orders").orderByChild("clientId").equalTo(SportifyApp.user.userId);
        query.addValueEventListener(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(orderAdapter);
    }
}
