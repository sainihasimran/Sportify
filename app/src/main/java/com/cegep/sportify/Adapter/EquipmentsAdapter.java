package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.EquipmentListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Equipment;
import java.util.Collection;
import java.util.List;

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentViewHolder> {

    private final Context context;

    private final List<Equipment> equipments;

    private final EquipmentListItemClickListener equipmentListItemClickListener;

    public EquipmentsAdapter(Context context, List<Equipment> equipments, EquipmentListItemClickListener equipmentListItemClickListener) {
        this.context = context;
        this.equipments = equipments;
        this.equipmentListItemClickListener = equipmentListItemClickListener;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(view, equipmentListItemClickListener);
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
