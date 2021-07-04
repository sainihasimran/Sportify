package com.cegep.sportify.checkout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.adapter.ShoppingCartAdapter;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.ShoppingCartItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShoppingCartFragment extends Fragment implements ShoppingCartChangeListener {

    private View totalPriceContainer;

    private TextView totalPriceTextView;

    private RecyclerView recyclerView;

    private ShoppingCartAdapter shoppingCartAdapter;

    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    private AlertDialog progress;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
            for (DataSnapshot shoppingCartSnapshot : snapshot.getChildren()) {
                ShoppingCartItem shoppingCartItem = shoppingCartSnapshot.getValue(ShoppingCartItem.class);
                shoppingCartItems.add(shoppingCartItem);
            }

            new FetchShoppingCartDetailsTask().execute(shoppingCartItems);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalPriceContainer = view.findViewById(R.id.total_price_container);

        totalPriceTextView = view.findViewById(R.id.total_price_text);
        setupRecyclerView(view);

        progress = new AlertDialog.Builder(requireContext(), R.style.LoadingDialogStyle)
                .setView(R.layout.item_loading)
                .setCancelable(false)
                .create();

        progress.show();

        Utils.getShoppingCartReference().addListenerForSingleValueEvent(valueEventListener);
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onShoppingCartChanged() {
        updateTotalPrice();
    }

    @Override
    public void onShoppingCartItemDeleted(ShoppingCartItem shoppingCartItem) {
        Utils.getShoppingCartReference().child(shoppingCartItem.getCartId()).removeValue();
    }

    private void showShoppingCartItemsList() {
        shoppingCartAdapter = new ShoppingCartAdapter(requireContext(), shoppingCartItems);
        recyclerView.setAdapter(shoppingCartAdapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        String totalPrice = "$" + String.format("%.2f", shoppingCartAdapter.getTotalPrice());
        totalPriceTextView.setText(totalPrice);
    }

    private class FetchShoppingCartDetailsTask extends AsyncTask<List<ShoppingCartItem>, Void, List<ShoppingCartItem>> {

        @Override
        protected List<ShoppingCartItem> doInBackground(List<ShoppingCartItem>... lists) {
            List<ShoppingCartItem> shoppingCartItems = lists[0];
            try {
                for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
                    if (shoppingCartItem.isProduct()) {
                        Task<DataSnapshot> fetchProductTask = Utils.getProductReference(shoppingCartItem.getProductId()).get();
                        DataSnapshot productDataSnapshot = Tasks.await(fetchProductTask);
                        Product product = productDataSnapshot.getValue(Product.class);
                        shoppingCartItem.setProduct(product);
                    } else {
                        Task<DataSnapshot> fetchEquipmentTask = Utils.getEquipmentReference(shoppingCartItem.getEquipmentId()).get();
                        DataSnapshot equipmentDataSnapshot = Tasks.await(fetchEquipmentTask);
                        Equipment equipment = equipmentDataSnapshot.getValue(Equipment.class);
                        shoppingCartItem.setEquipment(equipment);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return shoppingCartItems;
        }

        @Override
        protected void onPostExecute(List<ShoppingCartItem> shoppingCartItems) {
            super.onPostExecute(shoppingCartItems);

            ShoppingCartFragment.this.shoppingCartItems = shoppingCartItems;
            showShoppingCartItemsList();

            if (progress != null) {
                progress.dismiss();
            }

            totalPriceContainer.setVisibility(View.VISIBLE);
        }
    }
}
