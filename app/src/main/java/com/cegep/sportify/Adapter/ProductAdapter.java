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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final Context context;

    private final List<Product> products;

    private final ProductListItemClickListener productListItemClickListener;

    private List<String> favoriteProducts = new ArrayList<>();

    public ProductAdapter(Context context, List<Product> products, ProductListItemClickListener itemClickListener) {
        Collections.sort(products);
        Collections.reverse(products);

        this.context = context;
        this.products = products;
        this.productListItemClickListener = itemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, productListItemClickListener, favoriteProducts);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), context);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(Collection<Product> products, List<String> favoriteProducts) {
        List<Product> newProducts = new ArrayList<>(products);
        Collections.sort(newProducts);
        Collections.reverse(newProducts);

        this.products.clear();
        this.products.addAll(newProducts);

        this.favoriteProducts.clear();
        this.favoriteProducts.addAll(favoriteProducts);
        notifyDataSetChanged();
    }
}
