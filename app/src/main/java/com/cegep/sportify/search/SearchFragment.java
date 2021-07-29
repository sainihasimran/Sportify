package com.cegep.sportify.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.Adapter.ProductAdapter;
import com.cegep.sportify.Home.ProductsListFragment;
import com.cegep.sportify.ItemListItemClickListner;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.SearchItem;
import com.cegep.sportify.search.Adapter.SearchItemAdapter;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment implements ItemListItemClickListner {

    private SearchItemAdapter searchItemAdapter;

    private List<SearchItem> searchItems = new ArrayList<>();

    private View emptyView;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.getKey().equals("Products"))
            {
                for (DataSnapshot searchDataSnapshot : snapshot.getChildren()) {
                    SearchItem searchItem = new SearchItem();
                    searchItem.setProduct(searchDataSnapshot.getValue(Product.class));
                    searchItems.add(searchItem);
                }
            }
            if (snapshot.getKey().equals("Equipments"))
            {
                for (DataSnapshot searchDataSnapshot : snapshot.getChildren()) {
                    SearchItem searchItem = new SearchItem();
                    searchItem.setEquipment(searchDataSnapshot.getValue(Equipment.class));
                    searchItems.add(searchItem);
                }
            }
            Log.d("dsfds", snapshot.getValue().toString());

            SearchFragment.this.searchItems = searchItems;
            showItemList();
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
        setupRecyclerView(view);

        FirebaseDatabase adminappdb = Utils.getAdminDatabase();
        Query itemsReference;

        itemsReference = adminappdb.getReference("Products").orderByChild("createdAt");
        itemsReference.addValueEventListener(valueEventListener);
        itemsReference = adminappdb.getReference("Equipments").orderByChild("createdAt");
        itemsReference.addValueEventListener(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        searchItemAdapter = new SearchItemAdapter(requireContext(), this.searchItems,this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(searchItemAdapter);

    }

    private void showItemList() {

        Set<SearchItem> search = new HashSet<>();
        for (SearchItem searchItem : searchItems) {
                    search.add(searchItem);
            }

       emptyView.setVisibility(search.isEmpty() ? View.VISIBLE : View.GONE);

        searchItemAdapter.update(search);
    }

    @Override
    public void onItemClicked(SearchItem searchItem) {

    }
}