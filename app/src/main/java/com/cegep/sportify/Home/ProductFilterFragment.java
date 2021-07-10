package com.cegep.sportify.Home;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.cegep.sportify.Constants;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.ProductFilter;
import com.cegep.sportify.model.SportWithTeams;
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
    }

    private void setupBrandSpinner(View view) {
        Spinner brandChooser = view.findViewById(R.id.brand_chooser);
        final List<String> brand = new ArrayList<>();
        Utils.getSportWithTeamsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SportWithTeams> sportWithTeamsList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    SportWithTeams sportWithTeams = childSnapshot.getValue(SportWithTeams.class);
                    sportWithTeamsList.add(sportWithTeams);
                }

                for (SportWithTeams sportWithTeams : sportWithTeamsList) {
                    String remoteSport = sportWithTeams.getSport();
                    sports.add(Character.toUpperCase(remoteSport.charAt(0)) + remoteSport.substring(1));
                }

                sports.add(0, "All");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sports);
                sportsChooser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sportsChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                equipmentFilter.setSportFilter(sports.get(position));
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

    private void setupApplyButtonClick(View view) {
        view.findViewById(R.id.apply_button).setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof ProductFilterListener) {
                ((ProductFilterListener) activity).onProductFilterSelected(productFilter);
                Log.d("fragment", "cscdscdsfdfdsfsdfdsfdsfdfsdfs");
            }
            Log.d("fragment", "cscdscdsfdfdsfsdfdsfdsfdfsdfs");
            dismiss();
        });
    }
}
