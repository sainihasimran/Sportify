package com.cegep.sportify.SavedItems;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class savedequipmentAdapter extends RecyclerView.Adapter<savedequipmentAdapter.ViewHolder> {

    Context context;
    ArrayList<SavedItems> equipmentlist;
    DatabaseReference dbr;

    public savedequipmentAdapter(Context context, ArrayList<SavedItems> equipmentlist) {
        this.context = context;
        this.equipmentlist = equipmentlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SavedItems sv = equipmentlist.get(position);
        holder.pricetxt.setText(sv.getPricetxt());
        holder.nametxt.setText(sv.getNametxt());
        holder.saletxt.setText(sv.getSaletxt());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemimage,saleimg;
        TextView pricetxt,nametxt,saletxt;
        Button favbtn;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            itemimage = itemView.findViewById(R.id.item_image);
            saleimg = itemView.findViewById(R.id.sale_bg);
            pricetxt = itemView.findViewById(R.id.item_price);
            nametxt = itemView.findViewById(R.id.item_name);
            saletxt = itemView.findViewById(R.id.sale_text);
            favbtn = itemView.findViewById(R.id.favbtn);
        }
    }
}
