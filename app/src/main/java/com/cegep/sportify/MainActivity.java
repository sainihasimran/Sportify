package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cegep.sportify.SavedItems.ViewSaveditemsActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ProductsListFragment productListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        Toolbar topAppBar = findViewById(R.id.toolbar);
        topAppBar.inflateMenu(R.menu.menu_home);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_filter) {

                return true;
            } else if (item.getItemId() == R.id.action_search) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            else if (item.getItemId() == R.id.action_cart) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        topAppBar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profile) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.savedItems) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, ViewSaveditemsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
        showProductsFragment();
    }
    private void showProductsFragment() {
        productListFragment = new ProductsListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, productListFragment)
                .commit();
    }
}