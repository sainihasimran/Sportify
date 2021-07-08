package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.ProductListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Product;
import java.util.Collection;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final Context context;

    private final List<Product> products;

    private final ProductListItemClickListener productListItemClickListener;

    public ProductAdapter(Context context, List<Product> products, ProductListItemClickListener itemClickListener) {
        this.context = context;
        this.products = products;
        this.productListItemClickListener = itemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, productListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), context);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(Collection<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}
