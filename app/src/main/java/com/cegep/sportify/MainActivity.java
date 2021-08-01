package com.cegep.sportify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cegep.sportify.Home.EquipmentFilterFragment;
import com.cegep.sportify.Home.EquipmentFilterListener;
import com.cegep.sportify.Home.EquipmentsListFragment;
import com.cegep.sportify.Home.ProductFilterFragment;
import com.cegep.sportify.Home.ProductFilterListener;
import com.cegep.sportify.Home.ProductsListFragment;
import com.cegep.sportify.checkout.ShoppingCartActivity;
import com.cegep.sportify.model.EquipmentFilter;
import com.cegep.sportify.model.ProductFilter;
import com.cegep.sportify.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements ProductFilterListener, EquipmentFilterListener {

    private ProductsListFragment productListFragment;
    private EquipmentsListFragment equipmentsListFragment;
    private boolean isShowingProducts = true;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar topAppBar = findViewById(R.id.toolbar);
        topAppBar.inflateMenu(R.menu.menu_home);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_filter) {
                showFiltersFragment();
                return true;
            } else if (item.getItemId() == R.id.action_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.action_cart) {
                Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        topAppBar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.orders) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.profile) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        showProductsFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    @Override
    protected void onStart() {
        super.onStart();

        if (SportifyApp.productAddedInShoppingCart) {
            Snackbar.make(drawerLayout, "Product was successfully added to shopping cart", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show();
            SportifyApp.productAddedInShoppingCart = false;
        } else if (SportifyApp.equipmentAddedInShoppingCart) {
            Snackbar.make(drawerLayout, "Equipment was successfully added to shopping cart", Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show();
            SportifyApp.equipmentAddedInShoppingCart = false;
        }
    }

    private void showEquipmentsFragment() {
        equipmentsListFragment = new EquipmentsListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, equipmentsListFragment)
                .commit();
    }

    private void showProductsFragment() {
        productListFragment = new ProductsListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, productListFragment)
                .commit();
    }

    private void showFiltersFragment() {
        BottomSheetDialogFragment filterFragment;
        if (isShowingProducts) {
            filterFragment = new ProductFilterFragment();
            filterFragment.setTargetFragment(productListFragment, 0);
            filterFragment.show(getSupportFragmentManager(), null);
        } else {
            filterFragment = new EquipmentFilterFragment();
            filterFragment.setTargetFragment(equipmentsListFragment, 0);
            filterFragment.show(getSupportFragmentManager(), null);
        }
    }

    public void onProductFilterSelected(ProductFilter filter) {
        if (productListFragment != null) {
            productListFragment.handleFilters(filter);
        }
    }

    public void onEquipmentFilterSelected(EquipmentFilter filter) {
        if (equipmentsListFragment != null) {
            equipmentsListFragment.handleFilters(filter);
        }
    }
}