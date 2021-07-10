package com.cegep.sportify.SavedItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.ProductListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.details.productdetails.ProductDetailsActivity;
import com.cegep.sportify.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewSavedItemsFragment extends Fragment implements ProductListItemClickListener {

    public static Product selectedProduct = null;

    ViewSavedEquipmentsFragment viewsavedequipmentsFragment;
    private ArrayList<SavedItems> savedproducts;
    RecyclerView productrecyclerview;
    private View emptyView;
    saveditemadapter adapter;
    DatabaseReference dbr;
    private boolean isShowingProducts = true;

    public ViewSavedItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_saved_items, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);

        BottomNavigationView topNavigationView = view.findViewById(R.id.top_navigation);
        productrecyclerview = view.findViewById(R.id.productrecyclerView);
        dbr = FirebaseDatabase.getInstance().getReference();

        showProducts();
        topNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_product) {
                isShowingProducts = true;
                showProducts();
                return true;
            }

            if (item.getItemId() == R.id.action_equipment) {
                isShowingProducts = false;
                showEquipments();
                return true;
            }

            return false;
        });

    }

    private void showProducts() {

        savedproducts = new ArrayList<>();
        adapter = new saveditemadapter(getContext(),savedproducts);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //callback method on a database
        dbr.child("Users").child(uid).child("favoriteProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //fetch all the data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    SavedItems sv = dataSnapshot.getValue(SavedItems.class);
                    savedproducts.add(sv);//add fetched data to list
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productrecyclerview.setAdapter(adapter);
        productrecyclerview.setVisibility(View.VISIBLE);
    }

    private void showEquipments() {
        viewsavedequipmentsFragment = new ViewSavedEquipmentsFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, viewsavedequipmentsFragment)
                .commit();
    }

    public void onProductClicked(Product product) {
        selectedProduct = product;
        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
        startActivity(intent);
    }
}