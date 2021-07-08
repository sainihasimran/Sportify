package com.cegep.sportify.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QuantityFragment extends BottomSheetDialogFragment {

    int quantity = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quantity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView quantityTextView = view.findViewById(R.id.quantity_text);

        view.findViewById(R.id.increment_button).setOnClickListener(v -> {
            quantity += 1;
            quantityTextView.setText(String.valueOf(quantity));
        });

        view.findViewById(R.id.decrement_button).setOnClickListener(v -> {
            if (quantity == 1) {
                return;
            }

            quantity -= 1;
            quantityTextView.setText(String.valueOf(quantity));
        });

        view.findViewById(R.id.done_button).setOnClickListener(v -> {
            Fragment fragment = getTargetFragment();
            if (fragment instanceof QuantitySelectedListener) {
                ((QuantitySelectedListener) fragment).onQuantitySelected(quantity);
            }

            dismiss();
        });
    }
}
