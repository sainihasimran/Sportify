package com.cegep.sportify.checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cegep.sportify.R;

public class ShippingActivity extends AppCompatActivity {

    private static final String KEY_SELECTING_ADDRESS = "KEY_SELECTING_ADDRESS";

    public static Intent getCallingIntent(Context context, boolean isSelectingAddress) {
        Intent intent = new Intent(context, ShippingActivity.class);
        intent.putExtra(KEY_SELECTING_ADDRESS, isSelectingAddress);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        setupToolbar();

        if (savedInstanceState == null) {
            boolean isSelectingAddress = getIntent().getBooleanExtra(KEY_SELECTING_ADDRESS, false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, ShippingFragment.newInstance(isSelectingAddress))
                    .commit();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add) {
                Intent intent = AddressActivity.getCallingIntent(ShippingActivity.this, null);
                startActivity(intent);
                return true;
            }
            return false;
        });
        toolbar.setTitle(getIntent().getBooleanExtra(KEY_SELECTING_ADDRESS, false) ? R.string.shipping : R.string.addresses);
    }
}
