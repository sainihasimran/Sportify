package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cegep.sportify.Adapter.ProductAdapter;
import com.cegep.sportify.details.productdetails.ProductDetailsActivity;
import com.cegep.sportify.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProductsListFragment extends Fragment {

    public static Product selectedProduct = null;

    private View emptyView;

    private List<Product> products = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Product> products = new ArrayList<>();
            for (DataSnapshot productDataSnapshot : snapshot.getChildren()) {
                Product product = productDataSnapshot.getValue(Product.class);
                products.add(product);
            }
            ProductsListFragment.this.products = products;
            showProductList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    };

    private ProductAdapter productAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(view);

        FirebaseDatabase adminappdb = FirebaseDatabase.getInstance("https://sportify-admin-default-rtdb.firebaseio.com/");

        DatabaseReference productsReference = adminappdb.getReference("Products");
        productsReference.addValueEventListener(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        productAdapter= new ProductAdapter(requireContext(), this.products);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(productAdapter);
    }


    public void onClick(Product obj) {
        selectedProduct = obj;
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        intent.putExtra("product_name", obj.getProductName());
        requireActivity().startActivity(intent);
    }

    private void showProductList() {
        Set<Product> Products = new HashSet<>();
        for (Product product : products) {
            Products.add(product);
        }
        productAdapter.update(Products);
    }
}