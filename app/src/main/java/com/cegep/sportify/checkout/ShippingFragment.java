package com.cegep.sportify.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.Adapter.AddressAdapter;
import com.cegep.sportify.AddressSelectedListener;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Address;
import com.cegep.sportify.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ShippingFragment extends Fragment implements AddressSelectedListener {

    private static final String KEY_SELECTING_ADDRESS = "KEY_SELECTING_ADDRESS";

    private View noAddressContainer;
    private View addressContainer;
    private Button nextButton;
    private AlertDialog loadingDialog;

    private Address selectedAddress;

    private AddressAdapter addressAdapter;

    public static ShippingFragment newInstance(boolean isSelectingAddress) {
        ShippingFragment fragment = new ShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SELECTING_ADDRESS, isSelectingAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    private final ValueEventListener addressChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Address> addressList = new ArrayList<>();
            boolean hasSelectedAddress = false;
            for (DataSnapshot child : snapshot.getChildren()) {
                Address address = child.getValue(Address.class);
                if (selectedAddress != null) {
                    if (address != null && address.getId().equals(selectedAddress.getId())) {
                        address.setSelected(true);
                        selectedAddress = address;
                        hasSelectedAddress = true;
                    }
                }
                addressList.add(address);
            }

            if (!hasSelectedAddress) {
                selectedAddress = null;
            }

            addressAdapter.updateList(addressList);
            updateContainerVisibility(addressList.isEmpty());

            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingDialog = new AlertDialog.Builder(requireContext(), R.style.LoadingDialogStyle)
                .setView(R.layout.item_loading)
                .setCancelable(false)
                .show();

        noAddressContainer = view.findViewById(R.id.no_address_container);
        addressContainer = view.findViewById(R.id.address_container);
        nextButton = view.findViewById(R.id.next_button);

        setupAddressContainer(view);
        setupNextButton(view);

        Utils.getAddressReference().addValueEventListener(addressChangeListener);
    }

    @Override
    public void onAddressSelected(Address address) {
        if (selectedAddress != null) {
            selectedAddress.setSelected(false);
        }
        address.setSelected(true);
        selectedAddress = address;

        addressAdapter.refreshList();
    }

    @Override
    public void onEditAddressClicked(Address address) {
        Intent intent = AddressActivity.getCallingIntent(requireContext(), address.getId());
        startActivity(intent);
    }

    private void setupAddressContainer(View view) {
        boolean isSelectingAddress = getArguments().getBoolean(KEY_SELECTING_ADDRESS);
        addressAdapter = new AddressAdapter(this, isSelectingAddress);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addressAdapter);
    }

    private void setupNextButton(View view) {
        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Order order : SportifyApp.orders) {
                order.setAddress(selectedAddress);
            }

            Intent intent = new Intent(requireContext(), PaymentActivity.class);
            startActivity(intent);
        });
    }

    private void updateContainerVisibility(boolean addressListEmpty) {
        if (addressListEmpty) {
            noAddressContainer.setVisibility(View.VISIBLE);
            addressContainer.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        } else {
            noAddressContainer.setVisibility(View.GONE);
            addressContainer.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}
