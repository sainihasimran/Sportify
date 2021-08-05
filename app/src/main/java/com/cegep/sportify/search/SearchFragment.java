package com.cegep.sportify.search;


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

import com.cegep.sportify.ItemListItemClickListner;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.details.equipmentdetails.EquipmentDetailsActivity;
import com.cegep.sportify.details.productdetails.ProductDetailsActivity;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.SearchItem;
import com.cegep.sportify.search.Adapter.SearchItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment implements ItemListItemClickListner {

    public static Product selectedProduct = null;
    public static Equipment selectedEquipment = null;

    private SearchItemAdapter searchItemAdapter;

    private List<SearchItem> searchItems = new ArrayList<>();

    private View emptyView;
    private View noResult;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.getKey().equals("Products")) {
                for (DataSnapshot searchDataSnapshot : snapshot.getChildren()) {
                    SearchItem searchItem = new SearchItem();
                    searchItem.setProduct(searchDataSnapshot.getValue(Product.class));
                    searchItems.add(searchItem);
                }
            }
            if (snapshot.getKey().equals("Equipments")) {
                for (DataSnapshot searchDataSnapshot : snapshot.getChildren()) {
                    SearchItem searchItem = new SearchItem();
                    searchItem.setEquipment(searchDataSnapshot.getValue(Equipment.class));
                    searchItems.add(searchItem);
                }
            }
            SearchFragment.this.searchItems = searchItems;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);
        noResult = view.findViewById(R.id.no_result);
        //emptyView.setVisibility(View.VISIBLE);
        setupRecyclerView(view);

        FirebaseDatabase adminappdb = Utils.getAdminDatabase();
        Query itemsReference;

        itemsReference = adminappdb.getReference("Products").orderByChild("createdAt");
        itemsReference.addValueEventListener(valueEventListener);
        itemsReference = adminappdb.getReference("Equipments").orderByChild("createdAt");
        itemsReference.addValueEventListener(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        searchItemAdapter = new SearchItemAdapter(requireContext(), this.searchItems, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(searchItemAdapter);
    }

    private void showItemList(List<SearchItem> temp) {
        Set<SearchItem> search = new HashSet<>();
        for (SearchItem searchItem : temp) {
            search.add(searchItem);
        }
        noResult.setVisibility(search.isEmpty() ? View.VISIBLE : View.GONE);
        searchItemAdapter.update(search);
    }

    @Override
    public void onItemClicked(SearchItem searchItem) {
        Intent intent;
        if(searchItem.getProduct() != null)
        {
            selectedProduct = searchItem.getProduct();
            intent = new Intent(requireContext(), ProductDetailsActivity.class);
        }
        else
        {
            selectedEquipment = searchItem.getEquipment();
            intent = new Intent(requireContext(), EquipmentDetailsActivity.class);
        }
        startActivity(intent);
    }

    public void filter(String query) {
        emptyView.setVisibility(View.VISIBLE);
        if (!query.trim().isEmpty()) {

            List<SearchItem> temp = new ArrayList();
            temp.clear();
            for (SearchItem search : searchItems) {
                if (search.getProduct() != null) {
                    if (search.getProduct().getProductName().toLowerCase().contains(query.toLowerCase())) {
                        temp.add(search);
                    }
                } else if (search.getEquipment() != null) {
                    if (search.getEquipment().getEquipmentName().toLowerCase().contains(query.toLowerCase())) {
                        temp.add(search);
                    }
                }
            }
            searchItemAdapter.filter(temp);
        } else {
            emptyView.setVisibility(View.GONE);
            searchItemAdapter.filter(searchItems);
        }
    }
}