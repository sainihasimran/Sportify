package com.cegep.sportify;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AcceptedOrdersFragment extends Fragment {


    private OrderAdapter OrderAdapter;


    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Order> orders = new ArrayList<>();
            for (DataSnapshot orderDatasnapshot : snapshot.getChildren()) {
                Order order = orderDatasnapshot.getValue(Order.class);
                if (order != null && Utils.ORDER_ACCEPTED.equalsIgnoreCase(order.getStatus())) {
                    orders.add(order);
                }
            }
            Collections.sort(orders);
            Collections.reverse(orders);
            if (orders.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
            OrderAdapter.update(orders);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    private TextView emptyView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView = view.findViewById(R.id.empty_view);

        setupRecyclerView(view);
        FirebaseDatabase adminappdb = Utils.getClientDatabase();

        Query query = Utils.getClientDatabase().getReference("Orders").orderByChild("clientId").equalTo(SportifyApp.user.userId);
        query.addValueEventListener(valueEventListener);

    }

    private void setupRecyclerView(View view) {
        OrderAdapter = new OrderAdapter(requireContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(OrderAdapter);
    }

}
