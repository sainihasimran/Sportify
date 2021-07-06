package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DashboardFragment extends Fragment {

    private ProductsListFragment productListFragment;

    private boolean isShowingProducts = true;
    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProductsFragment();

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_clothing) {
                isShowingProducts = true;
                showProductsFragment();
                return true;
            }

            if (item.getItemId() == R.id.action_equipment) {
                isShowingProducts = false;
                showEquipmentsFragment();
                return true;
            }

            return false;
        });
    }

    private void showEquipmentsFragment() {
    }

    private void showProductsFragment() {
        productListFragment = new ProductsListFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, productListFragment)
                .commit();
    }

}