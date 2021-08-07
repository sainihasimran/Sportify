package com.cegep.sportify.details.productdetails;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cegep.sportify.Home.ProductsListFragment;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.ShippingActivity;
import com.cegep.sportify.details.QuantityFragment;
import com.cegep.sportify.details.QuantitySelectedListener;
import com.cegep.sportify.gallery.ImageAdapter;
import com.cegep.sportify.model.Admin;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.ShoppingCartItem;
import com.cegep.sportify.search.SearchFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
        this.product = ProductsListFragment.selectedProduct;

        setupProductName(view);
        setupProductImages(view);
        setupProductPrices(view);
        setupProductStatus(view);
        setupProductDescription(view);
        setupProductSizes(view);
        setupProductColors(view);
        setupAddToCart(view);
        setupBuyNow(view);
        setupReturnPolicy(view);
    }

    private void setupProductName(View view) {
        TextView textView = view.findViewById(R.id.product_title_text);
        textView.setText(product.getProductName());
    }

    private void setupProductImages(View view) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        WormDotsIndicator dotsIndicator = view.findViewById(R.id.dots_indicator);
        Group noImagesGroup = view.findViewById(R.id.no_images_group);

        if (product.getImages().isEmpty()) {
            noImagesGroup.setVisibility(View.VISIBLE);
            dotsIndicator.setVisibility(View.GONE);
        } else {
            noImagesGroup.setVisibility(View.GONE);

            ImageAdapter imageAdapter = new ImageAdapter(getChildFragmentManager(), product.getImages());
            viewPager.setAdapter(imageAdapter);
            dotsIndicator.setViewPager(viewPager);
            dotsIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void setupProductPrices(View view) {
        TextView priceTextView = view.findViewById(R.id.product_price);
        TextView originalPrice = view.findViewById(R.id.product_original_price);

        priceTextView.setText("$" + String.format("%.2f", product.getPrice()));

        if (product.isOnSale()) {
            float salePrice = product.getPrice() - (product.getPrice() * product.getSale()) / 100;
            String salePriceStr = "$" + String.format("%.2f", salePrice);
            priceTextView.setText(salePriceStr);
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrice.setVisibility(View.VISIBLE);
            originalPrice.setText("$" + String.format("%.2f", product.getPrice()));
        } else {
            originalPrice.setVisibility(View.GONE);
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
                View chipView = inflater.inflate(R.layout.color_selectable_chip, colorsRadioGroup, false);
                Chip chip = chipView.findViewById(R.id.chip);
                chip.setId(ViewCompat.generateViewId());
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedColor = color;
                    }
                });
                colorsRadioGroup.addView(chipView);
            }

            colorsRadioGroup.setVisibility(View.VISIBLE);
            noColorsTextView.setVisibility(View.GONE);
        } else {
            colorsRadioGroup.setVisibility(View.GONE);
            noColorsTextView.setVisibility(View.VISIBLE);
        }

        colorsRadioGroup.setSingleSelection(true);
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

    private void setupReturnPolicy(View view) {
        view.findViewById(R.id.return_policy_text).setOnClickListener(
                v -> Utils.getAdminDatabase().getReference("Admin").child(product.getAdminId())
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
        if (product.isOutOfStock()) {
            Toast.makeText(requireContext(), "Currently out of stock. Please check back at a later time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (product.hasColors() && TextUtils.isEmpty(selectedColor)) {
            Toast.makeText(requireContext(), "Please select a color", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!product.isOutOfStock() && TextUtils.isEmpty(selectedSize)) {
            Toast.makeText(requireContext(), "Please select a product size", Toast.LENGTH_SHORT).show();
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
            SportifyApp.isBuyMode = true;
            Order order = product.toOrder();
            order.setSize(selectedSize);
            order.setColor(selectedColor);
            order.setQuantity(quantity);
            order.setPrice(product.getFinalPrice() * quantity);

            SportifyApp.orders.clear();
            SportifyApp.orders.add(order);

            Intent intent = ShippingActivity.getCallingIntent(requireContext(), true);
            startActivity(intent);
        } else {
            Utils.getShoppingCartReference().orderByChild("productId").equalTo(product.getProductId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isNewShoppingCartItem = true;
                            ShoppingCartItem shoppingCartItem = null;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                shoppingCartItem = child.getValue(ShoppingCartItem.class);
                                if (shoppingCartItem == null) {
                                    continue;
                                }

                                boolean isColorSame = selectedColor.equals(shoppingCartItem.getColor());
                                boolean isSizeSame = selectedSize.equals(shoppingCartItem.getSize());
                                if (isColorSame && isSizeSame) {
                                    shoppingCartItem.setQuantity(quantity + shoppingCartItem.getQuantity());
                                    isNewShoppingCartItem = false;
                                    break;
                                }
                            }

                            if (shoppingCartItem == null || isNewShoppingCartItem) {
                                String cartId = Utils.getShoppingCartReference().push().getKey();

                                shoppingCartItem = new ShoppingCartItem();
                                shoppingCartItem.setColor(selectedColor);
                                shoppingCartItem.setSize(selectedSize);
                                shoppingCartItem.setProductId(product.getProductId());
                                shoppingCartItem.setClientId(SportifyApp.user.userId);
                                shoppingCartItem.setCartId(cartId);
                                shoppingCartItem.setQuantity(quantity);
                            }

                            Utils.getShoppingCartReference().child(shoppingCartItem.getCartId()).setValue(shoppingCartItem, (error, ref) -> {
                                if (error != null) {
                                    Toast.makeText(requireContext(), "Failed to add product to shopping cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    SportifyApp.productAddedInShoppingCart = true;
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
