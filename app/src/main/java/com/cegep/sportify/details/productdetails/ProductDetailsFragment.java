package com.cegep.sportify.details.productdetails;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.ShippingActivity;
import com.cegep.sportify.details.QuantityFragment;
import com.cegep.sportify.details.QuantitySelectedListener;
import com.cegep.sportify.gallery.ImageAdapter;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.ShoppingCartItem;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class ProductDetailsFragment extends Fragment implements QuantitySelectedListener {

    private Product product;

    private String selectedSize;

    private String selectedColor;

    private boolean isBuyMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupProductName(view);
        setupProductImages(view);
        setupProductPrices(view);
        setupProductStatus(view);
        setupProductDescription(view);
        setupProductSizes(view);
        setupProductColors(view);
        setupAddToCart(view);
        setupBuyNow(view);
    }

    private void setupProductName(View view) {
        TextView textView = view.findViewById(R.id.product_title_text);
        textView.setText(product.getProductName());
    }

    private void setupProductImages(View view) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        WormDotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewPager);
        Group noImagesGroup = view.findViewById(R.id.no_images_group);

        if (product.getImages().isEmpty()) {
            noImagesGroup.setVisibility(View.VISIBLE);
            dotsIndicator.setVisibility(View.GONE);
        } else {
            noImagesGroup.setVisibility(View.GONE);

            ImageAdapter imageAdapter = new ImageAdapter(getChildFragmentManager(), product.getImages());
            viewPager.setAdapter(imageAdapter);
        }
    }

    private void setupProductPrices(View view) {
        TextView priceTextView = view.findViewById(R.id.product_price);
        TextView saleTextView = view.findViewById(R.id.product_sale_price);

        priceTextView.setText("$" + String.format(".2f", product.getPrice()));

        if (product.isOnSale()) {
            float salePrice = product.getPrice() - (product.getPrice() * product.getSale()) / 100;
            String salePriceStr = "$" + String.format(".2f", salePrice);
            saleTextView.setText(salePriceStr);
            saleTextView.setPaintFlags(saleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            saleTextView.setVisibility(View.VISIBLE);
        } else {
            saleTextView.setVisibility(View.GONE);
        }
    }

    private void setupProductStatus(View view) {
        int color;
        String text;
        if (product.isOutOfStock()) {
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

    private void setupProductDescription(View view) {
        TextView descriptionTextView = view.findViewById(R.id.product_description);
        descriptionTextView.setText(product.getDescription());
    }

    private void setupProductSizes(View view) {
        RadioGroup sizesRadioGroup = view.findViewById(R.id.sizes_group);
        TextView noSizesTextView = view.findViewById(R.id.no_sizes_text);
        if (product.isOutOfStock()) {
            sizesRadioGroup.setVisibility(View.GONE);
            noSizesTextView.setVisibility(View.VISIBLE);
        } else {
            sizesRadioGroup.setVisibility(View.VISIBLE);
            noSizesTextView.setVisibility(View.GONE);

            RadioButton xSmallSizeButton = view.findViewById(R.id.xs_size_button);
            RadioButton smallSizeButton = view.findViewById(R.id.s_size_button);
            RadioButton mediumSizeButton = view.findViewById(R.id.m_size_button);
            RadioButton largeSizeButton = view.findViewById(R.id.l_size_button);
            RadioButton xLargeSizeButton = view.findViewById(R.id.xl_size_button);

            xSmallSizeButton.setEnabled(product.hasXSmallSize());
            smallSizeButton.setEnabled(product.hasSmallSize());
            mediumSizeButton.setEnabled(product.hasMediumSize());
            largeSizeButton.setEnabled(product.hasLargeSize());
            xLargeSizeButton.setEnabled(product.hasXLargeSize());

            sizesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.xs_size_button) {
                    selectedSize = "XS";
                } else if (checkedId == R.id.s_size_button) {
                    selectedSize = "S";
                } else if (checkedId == R.id.m_size_button) {
                    selectedSize = "M";
                } else if (checkedId == R.id.l_size_button) {
                    selectedSize = "L";
                } else if (checkedId == R.id.xl_size_button) {
                    selectedSize = "XL";
                }
            });
        }
    }

    private void setupProductColors(View view) {
        ChipGroup colorsRadioGroup = view.findViewById(R.id.colors_group);
        TextView noColorsTextView = view.findViewById(R.id.no_colors_text);
        if (product.hasColors()) {
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            for (final String color : product.getColors()) {
                View chip = inflater.inflate(R.layout.color_selectable_chip, colorsRadioGroup, false);
                chip.setOnClickListener(v -> selectedColor = color);
                colorsRadioGroup.addView(chip);
            }

            colorsRadioGroup.setVisibility(View.VISIBLE);
            noColorsTextView.setVisibility(View.GONE);
        } else {
            colorsRadioGroup.setVisibility(View.GONE);
            noColorsTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setupAddToCart(View view) {
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);
        addToCartButton.setEnabled(!product.isOutOfStock());
        addToCartButton.findViewById(R.id.add_to_cart_button).setOnClickListener(v -> {
            if (isProductValid()) {
                showQuantityFragment();
                isBuyMode = false;
            }
        });
    }

    private void setupBuyNow(View view) {
        Button buyNowButton = view.findViewById(R.id.buy_now_button);
        buyNowButton.setEnabled(!product.isOutOfStock());
        buyNowButton.findViewById(R.id.buy_now_button).setOnClickListener(v -> {
            if (isProductValid()) {
                showQuantityFragment();
                isBuyMode = true;
            }
        });
    }

    private boolean isProductValid() {
        if (product.isOutOfStock()) {
            Toast.makeText(requireContext(), "Currently out of stock. Check back at a later time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (product.hasColors() && TextUtils.isEmpty(selectedColor)) {
            Toast.makeText(requireContext(), "Please select a product size", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!product.isOutOfStock() && TextUtils.isEmpty(selectedSize)) {
            Toast.makeText(requireContext(), "Please select a color", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showQuantityFragment() {
        QuantityFragment quantityFragment = new QuantityFragment();
        quantityFragment.setTargetFragment(ProductDetailsFragment.this, 0);
        quantityFragment.show(getParentFragmentManager(), null);
    }

    @Override
    public void onQuantitySelected(int quantity) {
        if (isBuyMode) {
            Order order = product.toOrder();
            order.setSize(selectedSize);
            order.setColor(selectedColor);


            SportifyApp.orders.clear();
            SportifyApp.orders.add(order);

            Intent intent = new Intent(requireContext(), ShippingActivity.class);
            startActivity(intent);
        } else {
            String cartId = Utils.getShoppingCartReference().push().getKey();

            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setColor(selectedColor);
            shoppingCartItem.setSize(selectedSize);
            shoppingCartItem.setProductId(product.getProductId());
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
