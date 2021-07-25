package com.cegep.sportify.checkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.R;
import com.cegep.sportify.SearchAddressActivity;
import com.cegep.sportify.SearchAddressFragment;
import com.mapbox.search.result.SearchSuggestion;

public class AddressActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SEARCH_ADDRESS = 292;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                Intent intent = new Intent(AddressActivity.this, SearchAddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEARCH_ADDRESS);
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SEARCH_ADDRESS) {
            Parcelable parcelableExtra = data.getParcelableExtra(SearchAddressFragment.KEY_SELECTED_ADDRESS);
            if (parcelableExtra instanceof SearchSuggestion) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (fragment instanceof AddressFragment) {
                    ((AddressFragment) fragment).handleSearchSelection((SearchSuggestion) parcelableExtra);
                }
            }
        }
    }
}
