package com.cegep.sportify.checkout;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.cegep.sportify.MainActivity;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Address;
import com.cegep.sportify.model.CreditCard;
import com.cegep.sportify.model.Order;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {

    private EditText cardNameEditText;
    private EditText cardNumberEditText;
    private EditText expiryEditText;
    private EditText cvvEditText;

    private LottieAnimationView lottieAnimationView;

    private final CreditCard creditCard = new CreditCard();
    private CheckBox saveCardDetailsCheckBox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottieAnimationView = view.findViewById(R.id.lottie_animation_view);

        setupPaymentDetails(view);
        setupPaymentAmounts(view);
        setupAddress(view);
        setupPlaceOrderButton(view);

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

        saveCardDetailsCheckBox = view.findViewById(R.id.save_card_details_checkbox);
    }

    private void setupPaymentAmounts(View view) {
        TextView cartTotalTextView = view.findViewById(R.id.cart_total_text);
        TextView taxTextView = view.findViewById(R.id.tax_text);
        TextView totalTextView = view.findViewById(R.id.total_text);

        float cartTotal = 0f;
        for (Order order : SportifyApp.orders) {
            cartTotal += order.getPrice();
        }

        float taxAmount = (cartTotal * 15) / 100;
        float totalAmount = cartTotal + taxAmount;

        cartTotalTextView.setText("$" + String.format("%.2f", cartTotal));
        taxTextView.setText("$" + String.format("%.2f", taxAmount));
        totalTextView.setText("$" + String.format("%.2f", totalAmount));
    }

    private void setupAddress(View view) {
        Address address = SportifyApp.orders.get(0).getAddress();

        TextView fullNameTextView = view.findViewById(R.id.full_name_text);
        TextView addressLine1TextView = view.findViewById(R.id.address_line_1);
        TextView addressLine2TextView = view.findViewById(R.id.address_line_2);
        TextView postalCodeTextView = view.findViewById(R.id.postal_code_text);
        TextView phoneTextView = view.findViewById(R.id.phone_text);

        fullNameTextView.setText(address.getName());
        addressLine1TextView.setText(Utils.getAddressLine1(address));
        addressLine2TextView.setText(Utils.getAddressLine2(address));
        postalCodeTextView.setText(address.getPostalCode());
        phoneTextView.setText(address.getPhoneNumber());
    }

    private void setupPlaceOrderButton(View view) {
        view.findViewById(R.id.place_order_button).setOnClickListener(v -> {
            View currentFocusedView = view.findFocus();
            Activity activity = getActivity();
            if (currentFocusedView != null && activity != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
            if (creditCard.isValid(requireContext())) {
                boolean saveCardDetails = saveCardDetailsCheckBox.isChecked();
                if (saveCardDetails) {
                    Utils.getPaymentReference().setValue(creditCard, (error, ref) -> {
                        if (error != null) {
                            Toast.makeText(requireContext(), "Failed to save card details", Toast.LENGTH_SHORT).show();
                        } else {
                            placeOrders();
                        }
                    });
                } else {
                    placeOrders();
                }
            }
        });
    }

    private void placeOrders() {
        AlertDialog progress = new AlertDialog.Builder(requireContext(), R.style.LoadingDialogStyle)
                .setView(R.layout.item_loading)
                .setCancelable(false)
                .show();

        List<Task<?>> createOrderTasks = new ArrayList<>();
        List<Task<?>> removeShoppingCartItems = new ArrayList<>();
        for (Order order : SportifyApp.orders) {
            order.setCreditCard(creditCard);
            order.setCreatedAt(System.currentTimeMillis());
            createOrderTasks.add(Utils.getOrdersReference().child(order.getOrderId()).setValue(order));

            if (!TextUtils.isEmpty(order.getShoppingCartItemId())) {
                removeShoppingCartItems.add(Utils.getShoppingCartReference().child(order.getShoppingCartItemId()).removeValue());
            }
        }

        Task<?> createOrdersTask = Tasks.whenAllSuccess(createOrderTasks);
        Task<?> removeShoppingCartItemsTask = Tasks.whenAllSuccess(removeShoppingCartItems);

        if (!removeShoppingCartItems.isEmpty()) {
            createOrdersTask = createOrdersTask.continueWithTask(task -> removeShoppingCartItemsTask);
        }

        createOrdersTask.addOnSuccessListener(tasks -> {
            progress.dismiss();
            onOrderPlacedSuccessfully();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            progress.dismiss();
            Toast.makeText(requireContext(), "Failed to place orders", Toast.LENGTH_SHORT).show();
        });
    }

    private void onOrderPlacedSuccessfully() {
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.order_successful_popup_title)
                        .setMessage(R.string.order_successful_popup_message)
                        .setPositiveButton(R.string.ok, null)
                        .setCancelable(false)
                        .setOnDismissListener(dialog -> {
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        })
                        .show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
    }

    private void setSelection(EditText editText) {
        Selection.setSelection(editText.getText(), editText.length());
    }
}
