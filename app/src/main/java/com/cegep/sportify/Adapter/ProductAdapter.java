package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;
import com.cegep.sportify.model.Product;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final Context context;
    private final Product productobj;

    private final List<Product> products = new ArrayList<>();

    public ProductAdapter(Context context, Product clickedProduct) {
        this.context = context;
        this.productobj = clickedProduct;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view,productobj);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
       holder.bind(products.get(position), context);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
