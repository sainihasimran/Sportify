package com.cegep.sportify.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.cegep.sportify.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EquipmentsListFragment extends Fragment implements EquipmentListItemClickListener {

    public static Equipment selectedEquipment = null;

    private View emptyView;

    private List<Equipment> equipments = new ArrayList<>();

    private String adminID;

    private EquipmentFilter equipmentFilter = new EquipmentFilter();

    private List<String> favoriteEquipments = new ArrayList<>();

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Equipment> equipments = new ArrayList<>();
            for (DataSnapshot equipmentDataSnapshot : snapshot.getChildren()) {
                Equipment equipment = equipmentDataSnapshot.getValue(Equipment.class);
                equipments.add(equipment);
            }
            EquipmentsListFragment.this.equipments = equipments;
            showEquipmentList(true);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    private final ValueEventListener favoriteListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            EquipmentsListFragment.this.favoriteEquipments = (List<String>) snapshot.getValue();
            if (EquipmentsListFragment.this.favoriteEquipments == null) {
                EquipmentsListFragment.this.favoriteEquipments = new ArrayList<>();
            }

            showEquipmentList(false);
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
        FirebaseDatabase adminappdb = Utils.getAdminDatabase();

        Query equipmentsReference = adminappdb.getReference("Equipments").orderByChild("createdAt");
        equipmentsReference.addValueEventListener(valueEventListener);

        Utils.getFavoriteEquipmentsReference().addValueEventListener(favoriteListener);
    }

    private void setupRecyclerView(View view) {
        equipmentsAdapter = new EquipmentsAdapter(requireContext(), this.equipments, this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(equipmentsAdapter);
    }

    private void showEquipmentList(boolean fromEquipmentList) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        adminID = sharedPref.getString("adminid", "All");

        Set<Equipment> filteredEquipments = new HashSet<>();
        for (Equipment equipment : equipments) {
            String filterSale = equipmentFilter.getBrandFilter();
            if (equipmentFilter.getSportFilter().equals("All") || equipmentFilter.getSportFilter().equalsIgnoreCase(equipment.getSport())) {
                if(equipmentFilter.getBrandFilter().equals("All") || adminID.equals(equipment.getAdminId())) {
                    filteredEquipments.add(equipment);
                }
            }
        }

        if (equipmentFilter.getOnSale() != null) {
            boolean onSaleFilter = equipmentFilter.getOnSale();
            Iterator<Equipment> iterator = filteredEquipments.iterator();
            while (iterator.hasNext()) {
                Equipment equipment = iterator.next();
                if (equipment.isOnSale() != onSaleFilter) {
                    iterator.remove();
                }
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

        if (equipmentFilter.getFavorite() != null) {
            boolean favoriteFilter = equipmentFilter.getFavorite();
            Iterator<Equipment> iterator = filteredEquipments.iterator();
            while (iterator.hasNext()) {
                Equipment equipment = iterator.next();
                if (favoriteFilter) {
                    if (!favoriteEquipments.contains(equipment.getEquipmentId())) {
                        iterator.remove();
                    }
                } else {
                    if (favoriteEquipments.contains(equipment.getEquipmentId())) {
                        iterator.remove();
                    }
                }
            }
        }

        emptyView.setVisibility((fromEquipmentList && filteredEquipments.isEmpty()) ? View.VISIBLE : View.GONE);


        equipmentsAdapter.update(filteredEquipments, favoriteEquipments);
    }

    public void handleFilters(EquipmentFilter equipmentFilter) {
        this.equipmentFilter = equipmentFilter;
        showEquipmentList(true);
    }

    @Override
    public void onEquipmentClicked(Equipment equipment) {
        selectedEquipment = equipment;
        Intent intent = new Intent(requireContext(), EquipmentDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFavoriteButtonClicked(Equipment equipment, boolean favorite) {
        if (favorite) {
            favoriteEquipments.add(equipment.getEquipmentId());
        } else {
            favoriteEquipments.remove(equipment.getEquipmentId());
        }

        Utils.getFavoriteEquipmentsReference().setValue(favoriteEquipments);
    }
}
