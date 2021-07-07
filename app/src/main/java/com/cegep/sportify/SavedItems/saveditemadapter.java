package com.cegep.sportify.SavedItems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class saveditemadapter extends RecyclerView.Adapter<saveditemadapter.ViewHolder> {

    //declare variables
    Context context;
    ArrayList<SavedItems> itemlist;
    DatabaseReference db;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(context).inflate(R.layout.saveditem,parent,false);
        return new ViewHolder(view);
    }

    //constructor
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

        //TODO CHILD VALUE
        holder.favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.favbtn.getContext());
        builder.setTitle("Delete Item");
        builder.setMessage("Delete....?");
        builder.setPositiveButton("YES",((dialog, which) -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db =  FirebaseDatabase.getInstance().getReference();
            db.child("Users").child(uid).child("favoriteProducts")
           .removeValue();
        }));

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

            }
        });
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
