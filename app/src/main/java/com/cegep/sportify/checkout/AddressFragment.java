package com.cegep.sportify.checkout;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchSuggestion;
import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends Fragment {

    private EditText nameEditText;
    private EditText suiteNumberEditText;
    private EditText streetAddressEditText;
    private EditText cityEditText;
    private EditText provinceEditText;
    private EditText postalCodeEditText;
    private EditText phoneNumberEditText;

    private String addressId;

    private final Address address = new Address();

    private List<Address> addressList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.getAddressReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> addressList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Address address = child.getValue(Address.class);
                    addressList.add(address);
                }

                AddressFragment.this.addressList = addressList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupNameEditText(view);
        setupSuiteNumberEditText(view);
        setupStreetAddressEditText(view);
        setupCityEditText(view);
        setupProvinceEditText(view);
        setupPostalCodeEditText(view);
        setupPhoneNumberEditText(view);
        setupSaveButton(view);
    }

    private void setupNameEditText(View view) {
        nameEditText = view.findViewById(R.id.name_input);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSuiteNumberEditText(View view) {
        suiteNumberEditText = view.findViewById(R.id.suite_number_input);
        suiteNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setSuiteNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupStreetAddressEditText(View view) {
        streetAddressEditText = view.findViewById(R.id.street_address_input);
        streetAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setStreetAddress(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupCityEditText(View view) {
        cityEditText = view.findViewById(R.id.city_input);
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setCity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupProvinceEditText(View view) {
        provinceEditText = view.findViewById(R.id.province_input);
        provinceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setProvince(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPostalCodeEditText(View view) {
        postalCodeEditText = view.findViewById(R.id.postal_code_input);
        postalCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setPostalCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPhoneNumberEditText(View view) {
        phoneNumberEditText = view.findViewById(R.id.phone_number_input);
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                address.setPhoneNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaveButton(View view) {
        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            if (address.isValid(requireContext())) {
                if (TextUtils.isEmpty(addressId) && addressList.contains(address)) {
                    Toast.makeText(requireContext(), "Address already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                String addressId;
                if (!TextUtils.isEmpty(AddressFragment.this.addressId)) {
                    addressId = AddressFragment.this.addressId;
                } else {
                    addressId = Utils.getAddressReference().push().getKey();
                }

                address.setId(addressId);
                Utils.getAddressReference().child(addressId).setValue(address, (error, ref) -> {
                    if (error != null) {
                        Toast.makeText(requireContext(), "Failed to save address", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Address saved successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
            }
        });
    }

    private void setSelection(EditText editText) {
        Selection.setSelection(editText.getText(), editText.length());
    }

    public void handleAddressId(@Nullable String addressId) {
        if (TextUtils.isEmpty(addressId)) {
            return;
        }

        this.addressId = addressId;

        Utils.getAddressReference().child(addressId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address address = snapshot.getValue(Address.class);
                if (address != null) {
                    setupAddress(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void handleSearchSelection(SearchAddress searchAddress) {
        if (searchAddress == null) {
            return;
        }

        Address address = new Address();
        address.setName(nameEditText.getText().toString());
        address.setSuiteNumber(searchAddress.getHouseNumber());
        address.setStreetAddress(searchAddress.getStreet());
        address.setCity(searchAddress.getPlace());
        address.setProvince(searchAddress.getRegion());
        address.setPostalCode(searchAddress.getPostcode());
        address.setPhoneNumber(phoneNumberEditText.getText().toString());
        setupAddress(address);
    }

    private void setupAddress(Address address) {
        nameEditText.setText(address.getName());
        suiteNumberEditText.setText(address.getSuiteNumber());
        streetAddressEditText.setText(address.getStreetAddress());
        cityEditText.setText(address.getCity());
        provinceEditText.setText(address.getProvince());
        postalCodeEditText.setText(address.getPostalCode());
        phoneNumberEditText.setText(address.getPhoneNumber());

        setSelection(nameEditText);
        setSelection(suiteNumberEditText);
        setSelection(streetAddressEditText);
        setSelection(cityEditText);
        setSelection(provinceEditText);
        setSelection(postalCodeEditText);
        setSelection(phoneNumberEditText);
    }
}
