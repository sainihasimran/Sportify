package com.cegep.sportify.Home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cegep.sportify.Constants;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Brands;
import com.cegep.sportify.model.ProductFilter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductFilterFragment extends BottomSheetDialogFragment {
    private final ProductFilter productFilter = new ProductFilter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_filter, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCategoriesSpinner(view);
        setupSubCategoriesSpinner(view);
        setupBrandSpinner(view);
        setupOutOfStockChooser(view);
        setupOnSaleChooser(view);
        setupApplyButtonClick(view);
        setupFavoriteChooser(view);
    }

    private void setupBrandSpinner(View view) {
        Spinner brandsChooser = view.findViewById(R.id.brand_chooser);
        final List<String> brands = new ArrayList<>();
        List<Brands> brandsList = new ArrayList<>();
        Utils.getbrandReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brands.add(0, "All");
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Brands brand = childSnapshot.getValue(Brands.class);
                    String name = "" + childSnapshot.child("brandname").getValue();
                    String adminID = "" + childSnapshot.child("adminId").getValue();
                    brand.setBrand(name);
                    brand.setAdminID(adminID);
                    brandsList.add(brand);
                    brands.add(Character.toUpperCase(name.charAt(0)) + name.substring(1));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, brands);
                brandsChooser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        brandsChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productFilter.setBrandFilter(brands.get(position));
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (position > 0)
                    editor.putString("adminid", brandsList.get(position - 1).getAdminID());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCategoriesSpinner(View view) {
        List<String> categories = new ArrayList<>(Arrays.asList(Constants.CATEGORIES));
        categories.add(0, "All");
        Spinner categoryChooser = view.findViewById(R.id.category_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categoryChooser.setAdapter(adapter);
        categoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productFilter.setCategoryFilter(categories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSubCategoriesSpinner(View view) {
        List<String> subcategories = new ArrayList<>(Arrays.asList(Constants.SUB_CATEGORIES));
        subcategories.add(0, "All");
        Spinner subCategoryChooser = view.findViewById(R.id.sub_category_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, subcategories);
        subCategoryChooser.setAdapter(adapter);
        subCategoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productFilter.setSubCategoryFilter(subcategories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupOnSaleChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.on_sale_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.on_sale_none_button) {
                productFilter.setOnSale(null);
            } else if (checkedId == R.id.on_sale_yes_button) {
                productFilter.setOnSale(true);
            } else if (checkedId == R.id.on_sale_no_button) {
                productFilter.setOnSale(false);
            }
        });
    }


    private void setupOutOfStockChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.out_of_stock_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.out_of_stock_none_button) {
                productFilter.setOutOfStock(null);
            } else if (checkedId == R.id.out_of_stock_yes_button) {
                productFilter.setOutOfStock(true);
            } else if (checkedId == R.id.out_of_stock_no_button) {
                productFilter.setOutOfStock(false);
            }
        });
    }

    private void setupFavoriteChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.favorite_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.favorite_none_button) {
                productFilter.setFavorite(null);
            } else if (checkedId == R.id.favorite_yes_button) {
                productFilter.setFavorite(true);
            } else if (checkedId == R.id.favorite_no_button) {
                productFilter.setFavorite(false);
            }
        });
    }

    private void setupApplyButtonClick(View view) {
        view.findViewById(R.id.apply_button).setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof ProductFilterListener) {
                ((ProductFilterListener) activity).onProductFilterSelected(productFilter);
            }

            dismiss();
        });
    }
}
