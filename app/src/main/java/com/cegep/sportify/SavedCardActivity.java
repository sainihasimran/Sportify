package com.cegep.sportify;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cegep.sportify.R;

public class SavedCardActivity extends AppCompatActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_savedcard);

            setupToolbar();
        }

        private void setupToolbar() {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }


