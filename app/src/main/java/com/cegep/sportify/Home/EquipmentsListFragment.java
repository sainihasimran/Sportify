package com.cegep.sportify.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cegep.sportify.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.Adapter.EquipmentsAdapter;
import com.cegep.sportify.model.Equipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EquipmentsListFragment extends Fragment{

    public static Equipment selectedEquipment = null;

    private View emptyView;

    private List<Equipment> equipments = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Equipment> equipments = new ArrayList<>();
            for (DataSnapshot equipmentDataSnapshot : snapshot.getChildren()) {
                Equipment equipment = equipmentDataSnapshot.getValue(Equipment.class);
                equipments.add(equipment);
            }
            EquipmentsListFragment.this.equipments = equipments;
            showEquipmentList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    private EquipmentsAdapter equipmentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipments_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);

        setupRecyclerView(view);
        Query query = Utils.getEquipmentsReference().orderByChild("adminId").equalTo(SportifyAdminApp.admin.adminId);
        query.addValueEventListener(valueEventListener);

        view.findViewById(R.id.add_equipment_button).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEquipmentActivity.class);
            requireActivity().startActivity(intent);
        });
    }

    private void setupRecyclerView(View view) {
        equipmentsAdapter = new EquipmentsAdapter(requireContext(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(equipmentsAdapter);
    }

    @Override
    public void onClick(Equipment obj) {
        selectedEquipment = obj;
        Intent intent = new Intent(requireContext(), EditEquipmentActivity.class);
        requireActivity().startActivity(intent);
    }

    private void showEquipmentList() {
        Set<Equipment> filteredEquipments = new HashSet<>();
        for (Equipment equipment : equipments) {
            if (equipmentFilter.getSportFilter().equals("All") || equipmentFilter.getSportFilter().equalsIgnoreCase(equipment.getSport())) {
                filteredEquipments.add(equipment);
            }
        }

        if (equipmentFilter.getOutOfStock() != null) {
            boolean outOfStock = equipmentFilter.getOutOfStock();
            Iterator<Equipment> iterator = filteredEquipments.iterator();
            while (iterator.hasNext()) {
                Equipment equipment = iterator.next();
                if (equipment.isOutOfStock() != outOfStock) {
                    iterator.remove();
                }
            }
        }

        emptyView.setVisibility(filteredEquipments.isEmpty() ? View.VISIBLE : View.GONE);

        equipmentsAdapter.update(filteredEquipments);
    }

    public void handleFilters(EquipmentFilter equipmentFilter) {
        this.equipmentFilter = equipmentFilter;
        showEquipmentList();
    }
}
