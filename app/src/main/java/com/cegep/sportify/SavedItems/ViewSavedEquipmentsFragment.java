package com.cegep.sportify.SavedItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.EquipmentListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.details.equipmentdetails.EquipmentDetailsActivity;
import com.cegep.sportify.model.Equipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewSavedEquipmentsFragment extends Fragment implements EquipmentListItemClickListener {

    private ArrayList<SavedItems> savedequipments;
    RecyclerView recyclerview;
    savedequipmentAdapter eqadapter;
    DatabaseReference dbr;

    public static Equipment selectedEquipment = null;

    public ViewSavedEquipmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_saved_equipments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerview = view.findViewById(R.id.recyclerView);
        savedequipments = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbr = FirebaseDatabase.getInstance().getReference();

        //callback method on a database
        dbr.child("Users").child(uid).child("favoriteEquipments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //fetch all the data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    SavedItems sv = dataSnapshot.getValue(SavedItems.class);
                    savedequipments.add(sv);//add fetched data to list
                }
                eqadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eqadapter = new savedequipmentAdapter(getContext(),savedequipments);
        recyclerview.setAdapter(eqadapter);
        recyclerview.setVisibility(View.VISIBLE);

    }

    public void onEquipmentClicked(Equipment equipment) {
        selectedEquipment = equipment;
        Intent intent = new Intent(requireContext(), EquipmentDetailsActivity.class);
        startActivity(intent);
    }
}