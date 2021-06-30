package com.cegep.sportify.productdetails;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cegep.sportify.R;
import com.cegep.sportify.gallery.ImageAdapter;
import com.cegep.sportify.model.Product;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class ProductDetailsFragment extends Fragment {

    private Product product;

    private String selectedSize;

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
        RadioGroup colorsRadioGroup = view.findViewById(R.id.colors_group);
        TextView noColorsTextView = view.findViewById(R.id.no_colors_text);
        if (product.hasColors()) {
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
        addToCartButton.findViewById(R.id.add_to_cart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupBuyNow(View view) {
        Button buyNowButton = view.findViewById(R.id.buy_now_button);
        buyNowButton.setEnabled(!product.isOutOfStock());
        buyNowButton.findViewById(R.id.buy_now_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
