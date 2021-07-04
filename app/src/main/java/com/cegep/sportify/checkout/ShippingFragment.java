package com.cegep.sportify.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ShippingFragment extends Fragment {

    private View noAddressContainer;
    private View addressContainer;
    private Button nextButton;

    private Address address;

    private TextView fullNameTextView;
    private TextView addressLine1TextView;
    private TextView addressLine2TextView;
    private TextView emailTextView;
    private TextView phoneTextView;

    private final ValueEventListener addressChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            ShippingFragment.this.address = snapshot.getValue(Address.class);
            updateContainerVisibility();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private final View.OnClickListener saveAddressClickListener = v -> {
        Intent intent = new Intent(requireContext(), AddressActivity.class);
        startActivity(intent);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noAddressContainer = view.findViewById(R.id.no_address_container);
        addressContainer = view.findViewById(R.id.address_container);
        nextButton = view.findViewById(R.id.next_button);

        setupNoAddressContainer(view);
        setupAddressContainer(view);
        setupNextButton(view);

        Utils.getAddressReference().addValueEventListener(addressChangeListener);
    }

    private void setupNoAddressContainer(View view) {
        view.findViewById(R.id.add_address_button).setOnClickListener(saveAddressClickListener);
    }

    private void setupAddressContainer(View view) {
        view.findViewById(R.id.edit_button).setOnClickListener(saveAddressClickListener);

        fullNameTextView = view.findViewById(R.id.full_name_text);
        addressLine1TextView = view.findViewById(R.id.address_line_1);
        addressLine2TextView = view.findViewById(R.id.address_line_2);
        emailTextView = view.findViewById(R.id.email_text);
        phoneTextView = view.findViewById(R.id.phone_text);
    }

    private void setupNextButton(View view) {
        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void updateContainerVisibility() {
        if (address == null) {
            noAddressContainer.setVisibility(View.VISIBLE);
            addressContainer.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        } else {
            fullNameTextView.setText(SportifyApp.user.getFullname());
            addressLine1TextView.setText(address.getSuiteNumber() + ", " + address.getStreetAddress());
            addressLine2TextView.setText(address.getCity() + ", " + address.getProvince());
            emailTextView.setText(SportifyApp.user.email);
            phoneTextView.setText(address.getPhoneNumber());

            noAddressContainer.setVisibility(View.GONE);
            addressContainer.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}
