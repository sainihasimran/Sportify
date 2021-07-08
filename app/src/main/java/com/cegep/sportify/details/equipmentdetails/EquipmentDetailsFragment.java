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
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.ShippingActivity;
import com.cegep.sportify.details.QuantityFragment;
import com.cegep.sportify.details.QuantitySelectedListener;
import com.cegep.sportify.gallery.ImageAdapter;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.ShoppingCartItem;
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
        }
    }

    private void setupEquipmentPrices(View view) {
        TextView priceTextView = view.findViewById(R.id.equipment_price);
        TextView saleTextView = view.findViewById(R.id.equipment_sale_price);

        priceTextView.setText("$" + String.format("%.2f", equipment.getPrice()));

        if (equipment.isOnSale()) {
            float salePrice = equipment.getPrice() - (equipment.getPrice() * equipment.getSale()) / 100;
            String salePriceStr = "$" + String.format(".2f", salePrice);
            saleTextView.setText(salePriceStr);
            saleTextView.setPaintFlags(saleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            saleTextView.setVisibility(View.VISIBLE);
        } else {
            saleTextView.setVisibility(View.GONE);
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

            SportifyApp.orders.clear();
            SportifyApp.orders.add(order);

            Intent intent = new Intent(requireContext(), ShippingActivity.class);
            startActivity(intent);
        } else {
            String cartId = Utils.getShoppingCartReference().push().getKey();

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setSport(equipment.getSport());
            shoppingCartItem.setEquipmentId(equipment.getEquipmentId());
            shoppingCartItem.setClientId(SportifyApp.user.userId);
            shoppingCartItem.setCartId(cartId);
            shoppingCartItem.setQuantity(quantity);

            Utils.getShoppingCartReference().child(cartId).setValue(shoppingCartItem, (error, ref) -> {
                if (error != null) {
                    Toast.makeText(requireContext(), "Failed to add product to shopping cart", Toast.LENGTH_SHORT).show();
                } else {
                    requireActivity().finish();
                }
            });
        }
    }
}
