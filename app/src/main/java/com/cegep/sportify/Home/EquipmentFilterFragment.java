package com.cegep.sportify.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cegep.sportify.R;
import com.cegep.sportify.model.EquipmentFilter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

       
    }


}