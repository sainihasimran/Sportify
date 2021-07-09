package com.cegep.sportify.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.Adapter.EquipmentsAdapter;
import com.cegep.sportify.EquipmentListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.details.equipmentdetails.EquipmentDetailsActivity;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.EquipmentFilter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EquipmentsListFragment extends Fragment implements EquipmentListItemClickListener {

    public static Equipment selectedEquipment = null;

    private List<Equipment> equipments = new ArrayList<>();

    private EquipmentFilter equipmentFilter = new EquipmentFilter();

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

        setupRecyclerView(view);
        FirebaseDatabase adminappdb = Utils.getAdminDatabase();

        DatabaseReference equipmentsReference = adminappdb.getReference("Equipments");
        equipmentsReference.addValueEventListener(valueEventListener);

    }

    private void setupRecyclerView(View view) {
        equipmentsAdapter = new EquipmentsAdapter(requireContext(), this.equipments, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(equipmentsAdapter);
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
        equipmentsAdapter.update(filteredEquipments);
    }

    public void handleFilters(EquipmentFilter equipmentFilter) {
        this.equipmentFilter = equipmentFilter;
        showEquipmentList();
    }

    @Override
    public void onEquipmentClicked(Equipment equipment) {
        selectedEquipment = equipment;
        Intent intent = new Intent(requireContext(), EquipmentDetailsActivity.class);
        startActivity(intent);
    }
}
