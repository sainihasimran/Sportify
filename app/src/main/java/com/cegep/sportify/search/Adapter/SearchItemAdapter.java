package com.cegep.sportify.search.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.ProductListItemClickListener;
import com.cegep.sportify.R;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.SearchItem;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemViewHolder> {


    private final Context context;

    private final List<SearchItem> searchItems;

    private final ProductListItemClickListener productListItemClickListener;

    public SearchItemAdapter(Context context, List<SearchItem> searchItems, ProductListItemClickListener itemClickListener) {
        this.context = context;
        this.searchItems = searchItems;
        this.productListItemClickListener = itemClickListener;
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new SearchItemViewHolder(view, productListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchItemViewHolder holder, int position) {
        holder.bind(searchItems.get(position), context);
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public void update(Collection<SearchItem> searchItems, List<String> favoriteProducts) {
        this.searchItems.clear();
        this.searchItems.addAll(searchItems);
        notifyDataSetChanged();
    }
}
