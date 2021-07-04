package com.cegep.sportify.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.R;
import com.cegep.sportify.model.ShoppingCartItem;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartViewHolder> {

    private final Context context;

    private final List<ShoppingCartItem> shoppingCartItems;

    public ShoppingCartAdapter(Context context, List<ShoppingCartItem> shoppingCartItems) {
        this.context = context;
        this.shoppingCartItems = shoppingCartItems;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_shopping_cart_item, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        holder.bind(context, shoppingCartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return shoppingCartItems.size();
    }

    public float getTotalPrice() {
        float totalPrice = 0f;
        for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            totalPrice += shoppingCartItem.getTotalPrice();
        }

        return totalPrice;
    }
}
