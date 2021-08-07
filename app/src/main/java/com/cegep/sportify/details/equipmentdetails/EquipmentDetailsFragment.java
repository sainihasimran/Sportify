package com.cegep.sportify.details.equipmentdetails;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cegep.sportify.Home.EquipmentsListFragment;
import com.cegep.sportify.Home.ProductsListFragment;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.ShippingActivity;
import com.cegep.sportify.details.QuantityFragment;
import com.cegep.sportify.details.QuantitySelectedListener;
import com.cegep.sportify.gallery.ImageAdapter;
import com.cegep.sportify.model.Admin;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.ShoppingCartItem;
import com.cegep.sportify.search.SearchFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class EquipmentDetailsFragment extends Fragment implements QuantitySelectedListener {

    private Equipment equipment;

    private boolean isBuyMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.equipment = EquipmentsListFragment.selectedEquipment;

        setupEquipmentName(view);
        setupEquipmentImages(view);
        setupEquipmentPrices(view);
        setupEquipmentStatus(view);
        setupEquipmentDescription(view);
        setupEquipmentSport(view);
        setupAddToCart(view);
        setupBuyNow(view);
        setupReturnPolicy(view);
    }

    private void setupEquipmentName(View view) {
        TextView textView = view.findViewById(R.id.equipment_title_text);
        textView.setText(equipment.getEquipmentName());
    }

    private void setupEquipmentImages(View view) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        WormDotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        Group noImagesGroup = view.findViewById(R.id.no_images_group);

        if (equipment.getImages().isEmpty()) {
            noImagesGroup.setVisibility(View.VISIBLE);
            dotsIndicator.setVisibility(View.GONE);
        } else {
            noImagesGroup.setVisibility(View.GONE);

            ImageAdapter imageAdapter = new ImageAdapter(getChildFragmentManager(), equipment.getImages());
            viewPager.setAdapter(imageAdapter);
            dotsIndicator.setViewPager(viewPager);
            dotsIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void setupEquipmentPrices(View view) {
        TextView priceTextView = view.findViewById(R.id.equipment_price);
        TextView originalPrice = view.findViewById(R.id.equipment_original_price);

        priceTextView.setText("$" + String.format("%.2f", equipment.getPrice()));

        if (equipment.isOnSale()) {
            float salePrice = equipment.getPrice() - (equipment.getPrice() * equipment.getSale()) / 100;
            String salePriceStr = "$" + String.format("%.2f", salePrice);
            priceTextView.setText(salePriceStr);
            originalPrice.setText(salePriceStr);
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrice.setVisibility(View.VISIBLE);
            originalPrice.setText("$" + String.format("%.2f", equipment.getPrice()));
        } else {
            originalPrice.setVisibility(View.GONE);
        }
    }

    private void setupEquipmentStatus(View view) {
        int color;
        String text;
        if (equipment.isOutOfStock()) {
            color = getResources().getColor(R.color.faded_red);
            text = getString(R.string.out_of_stock);
        } else {
            color = getResources().getColor(R.color.green);
            text = getString(R.string.in_stock);
        }

        TextView statusTextView = view.findViewById(R.id.stock_text);
        statusTextView.setTextColor(color);
        statusTextView.setText(text);
    }

    private void setupEquipmentDescription(View view) {
        TextView descriptionTextView = view.findViewById(R.id.equipment_description);
        descriptionTextView.setText(equipment.getDescription());
    }

    private void setupEquipmentSport(View view) {
        TextView sportTextView = view.findViewById(R.id.equipment_sport);
        sportTextView.setText(equipment.getSport());
    }

    private void setupAddToCart(View view) {
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);
        addToCartButton.setEnabled(!equipment.isOutOfStock());
        addToCartButton.findViewById(R.id.add_to_cart_button).setOnClickListener(v -> {
            if (isProductValid()) {
                showQuantityFragment();
                isBuyMode = false;
            }
        });
    }

    private void setupBuyNow(View view) {
        Button buyNowButton = view.findViewById(R.id.buy_now_button);
        buyNowButton.setEnabled(!equipment.isOutOfStock());
        buyNowButton.findViewById(R.id.buy_now_button).setOnClickListener(v -> {
            if (isProductValid()) {
                showQuantityFragment();
                isBuyMode = true;
            }
        });
    }

    private void setupReturnPolicy(View view) {
        view.findViewById(R.id.return_policy_text).setOnClickListener(
                v -> Utils.getAdminDatabase().getReference("Admin").child(equipment.getAdminId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Admin admin = snapshot.getValue(Admin.class);
                                if (admin != null) {
                                    Utils.launchWebpage(admin.returnPolicyUrl, requireActivity());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }));
    }

    private boolean isProductValid() {
        if (equipment.isOutOfStock()) {
            Toast.makeText(requireContext(), "Currently out of stock. Please check back at a later time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showQuantityFragment() {
        QuantityFragment quantityFragment = new QuantityFragment();
        quantityFragment.setTargetFragment(EquipmentDetailsFragment.this, 0);
        quantityFragment.show(getParentFragmentManager(), null);
    }

    @Override
    public void onQuantitySelected(int quantity) {
        if (isBuyMode) {
            SportifyApp.isBuyMode = true;
            Order order = equipment.toOrder();
            order.setQuantity(quantity);
            order.setPrice(equipment.getFinalPrice() * quantity);

            SportifyApp.orders.clear();
            SportifyApp.orders.add(order);

            Intent intent = ShippingActivity.getCallingIntent(requireContext(), true);
            startActivity(intent);
        } else {
            Utils.getShoppingCartReference().orderByChild("equipmentId").equalTo(equipment.getEquipmentId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ShoppingCartItem shoppingCartItem = null;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                shoppingCartItem = child.getValue(ShoppingCartItem.class);
                            }

                            if (shoppingCartItem == null) {
                                String cartId = Utils.getShoppingCartReference().push().getKey();

                                shoppingCartItem = new ShoppingCartItem();
                                shoppingCartItem.setSport(equipment.getSport());
                                shoppingCartItem.setEquipmentId(equipment.getEquipmentId());
                                shoppingCartItem.setClientId(SportifyApp.user.userId);
                                shoppingCartItem.setCartId(cartId);
                                shoppingCartItem.setQuantity(quantity);
                            } else {
                                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + quantity);
                            }

                            Utils.getShoppingCartReference().child(shoppingCartItem.getCartId()).setValue(shoppingCartItem, (error, ref) -> {
                                if (error != null) {
                                    Toast.makeText(requireContext(), "Failed to add product to shopping cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    SportifyApp.equipmentAddedInShoppingCart = true;
                                    requireActivity().finish();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}
