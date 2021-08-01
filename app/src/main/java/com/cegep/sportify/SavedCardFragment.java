package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cegep.sportify.model.CreditCard;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class SavedCardFragment extends Fragment {

    private EditText cardNameEditText;
    private EditText cardNumberEditText;
    private EditText expiryEditText;
    private EditText cvvEditText;

    private final CreditCard creditCard = new CreditCard();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_savedcard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPaymentDetails(view);
        setupSavedCardButton(view);

        Utils.getPaymentReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CreditCard creditCard = snapshot.getValue(CreditCard.class);
                if (creditCard != null) {
                    cardNameEditText.setText(creditCard.getNameOnCard());
                    cardNumberEditText.setText(creditCard.getCardNumber());
                    expiryEditText.setText(creditCard.getExpiry());
                    cvvEditText.setText(creditCard.getCvv());

                    setSelection(cardNameEditText);
                    setSelection(cardNumberEditText);
                    setSelection(expiryEditText);
                    setSelection(cvvEditText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSelection(EditText editText) {
        Selection.setSelection(editText.getText(), editText.length());
    }

    private void setupPaymentDetails(View view) {
        cardNameEditText = view.findViewById(R.id.card_name_input);
        cardNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCard.setNameOnCard(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardNumberEditText = view.findViewById(R.id.card_number_input);
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCard.setCardNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        expiryEditText = view.findViewById(R.id.expiry_date_input);
        expiryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCard.setExpiry(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvvEditText = view.findViewById(R.id.cvv_input);
        cvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCard.setCvv(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupSavedCardButton(View view) {
        view.findViewById(R.id.update_button).setOnClickListener(v -> {
            if (creditCard.isValid(requireContext())) {
                Utils.getPaymentReference().setValue(creditCard, (error, ref) -> {
                    if (error != null) {
                        Toast.makeText(requireContext(), "Failed to save card details", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Saved Card details Successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
            }
        });
    }
    }

