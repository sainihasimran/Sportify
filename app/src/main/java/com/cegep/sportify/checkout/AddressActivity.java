package com.cegep.sportify.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.R;
import com.cegep.sportify.SearchAddressActivity;
import com.cegep.sportify.SearchAddressFragment;
import com.cegep.sportify.Utils;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchSuggestion;

public class AddressActivity extends AppCompatActivity {

    private static final String KEY_ADDRESS_ID = "KEY_ADDRESS_ID";

    public static final int REQUEST_CODE_SEARCH_ADDRESS = 292;

    public static Intent getCallingIntent(Context context, String addressId) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra(KEY_ADDRESS_ID, addressId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setupToolbar();

        String addressId = getIntent().getStringExtra(KEY_ADDRESS_ID);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof AddressFragment) {
            ((AddressFragment) fragment).handleAddressId(addressId);
        }
    }

    private void setupToolbar() {
        String addressId = getIntent().getStringExtra(KEY_ADDRESS_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                Intent intent = new Intent(AddressActivity.this, SearchAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEARCH_ADDRESS);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_address_title)
                        .setMessage(R.string.delete_address_message)
                        .setPositiveButton(R.string.delete, (dialog, which) -> deleteAddress(addressId))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }

            return false;
        });

        if (!TextUtils.isEmpty(addressId)) {
            MenuItem deleteMenuItem = toolbar.getMenu().findItem(R.id.action_delete);
            deleteMenuItem.setVisible(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SEARCH_ADDRESS) {
            Parcelable parcelableExtra = data.getParcelableExtra(SearchAddressFragment.KEY_SELECTED_ADDRESS);
            if (parcelableExtra instanceof SearchAddress) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (fragment instanceof AddressFragment) {
                    ((AddressFragment) fragment).handleSearchSelection((SearchAddress) parcelableExtra);
                }
            }
        }
    }

    private void deleteAddress(String addressId) {
        Utils.getAddressReference().child(addressId).removeValue();
        finish();
    }
}
