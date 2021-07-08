package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Equipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentViewHolder> {

    private final Context context;

    private final List<Equipment> equipments;

    public EquipmentsAdapter(Context context, List<Equipment> equipments) {
        this.context = context;
        this.equipments = equipments;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        holder.bind(equipments.get(position), context);
    }

    @Override
    public int getItemCount() {
        return equipments.size();
    }

    public void update(Collection<Equipment> equipments) {
        this.equipments.clear();
        this.equipments.addAll(equipments);
        notifyDataSetChanged();
    }
}
