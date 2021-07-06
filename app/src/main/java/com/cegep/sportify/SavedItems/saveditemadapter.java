package com.cegep.sportify.SavedItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;

import java.util.ArrayList;

public class saveditemadapter extends RecyclerView.Adapter<saveditemadapter.ViewHolder> {

    Context context;
    ArrayList<SavedItems> itemlist;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(context).inflate(R.layout.saveditem,parent,false);
   
        return new ViewHolder(view);
    }

    public saveditemadapter(Context context, ArrayList<SavedItems> itemlist) {
        this.context = context;
        this.itemlist = itemlist;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SavedItems sv = itemlist.get(position);
        holder.pricetxt.setText(sv.getPricetxt());
        holder.nametxt.setText(sv.getNametxt());
        holder.saletxt.setText(sv.getSaletxt());
    }



    @Override
    public int getItemCount() {
        
        return itemlist.size();
    }


    // viewholder class
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
