package com.cegep.sportify.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.Adapter.ProductAdapter;
import com.cegep.sportify.ProductListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.details.productdetails.ProductDetailsActivity;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.ProductFilter;
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

public class ProductsListFragment extends Fragment implements ProductListItemClickListener {

    public static Product selectedProduct = null;

    private List<Product> products = new ArrayList<>();

    private ProductFilter productFilter = new ProductFilter();

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
        public void onCancelled(@NonNull DatabaseError error) {
        }
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

        FirebaseDatabase adminappdb = Utils.getAdminDatabase();

        DatabaseReference productsReference = adminappdb.getReference("Products");
        productsReference.addValueEventListener(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        productAdapter = new ProductAdapter(requireContext(), this.products, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(productAdapter);
    }

    private void showProductList() {
        Set<Product> filteredProducts = new HashSet<>();
        for (Product product : products) {
            String filterCategory = "Outerwear";//productFilter.getCategoryFilter();
            String filterSubCategory = "Women's";//productFilter.getSubCategoryFilter();
            if (filterCategory.equals("All") || filterCategory.equals(product.getCategory())) {
                if (filterSubCategory.equals("All") || filterSubCategory.equals(product.getSubCategory())) {
                    filteredProducts.add(product);
                }
            }
        }
        if (productFilter.getOutOfStock() != null) {
            boolean outOfStock = productFilter.getOutOfStock();
            Iterator<Product> iterator = filteredProducts.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (product.isOutOfStock() != outOfStock) {
                    iterator.remove();
                }
            }
        }
        productAdapter.update(filteredProducts);
    }

    public void handleFilters(ProductFilter productFilter) {
        this.productFilter = productFilter;
        showProductList();
    }

    @Override
    public void onProductClicked(Product product) {
        selectedProduct = product;
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        startActivity(intent);
    }
}