package com.cegep.sportify.Home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Brands;
import com.cegep.sportify.model.EquipmentFilter;
import com.cegep.sportify.model.SportWithTeams;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EquipmentFilterFragment extends BottomSheetDialogFragment {

    private final EquipmentFilter equipmentFilter = new EquipmentFilter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSportChooser(view);
        setupBrandSpinner(view);
        setupOnSaleChooser(view);
        setupOutOfStockChooser(view);
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
                equipmentFilter.setBrandFilter(brands.get(position));
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (position>0)
                    editor.putString("adminid", brandsList.get(position-1).getAdminID());
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupSportChooser(View view) {
        Spinner sportsChooser = view.findViewById(R.id.sport_chooser);
        final List<String> sports = new ArrayList<>();
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

    private void setupOnSaleChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.on_sale_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.on_sale_none_button) {
                equipmentFilter.setOnSale(null);
            } else if (checkedId == R.id.on_sale_yes_button) {
                equipmentFilter.setOnSale(true);
            } else if (checkedId == R.id.on_sale_no_button) {
                equipmentFilter.setOnSale(false);
            }
        });
    }

    private void setupOutOfStockChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.out_of_stock_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.out_of_stock_none_button) {
                equipmentFilter.setOutOfStock(null);
            } else if (checkedId == R.id.out_of_stock_yes_button) {
                equipmentFilter.setOutOfStock(true);
            } else if (checkedId == R.id.out_of_stock_no_button) {
                equipmentFilter.setOutOfStock(false);
            }
        });
    }

    private void setupFavoriteChooser(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.favorite_chooser);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.favorite_none_button) {
                equipmentFilter.setFavorite(null);
            } else if (checkedId == R.id.favorite_yes_button) {
                equipmentFilter.setFavorite(true);
            } else if (checkedId == R.id.favorite_no_button) {
                equipmentFilter.setFavorite(false);
            }
        });
    }

    private void setupApplyButtonClick(View view) {
        view.findViewById(R.id.apply_button).setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof EquipmentFilterListener) {
                ((EquipmentFilterListener) activity).onEquipmentFilterSelected(equipmentFilter);
            }
            dismiss();
        });
    }
}