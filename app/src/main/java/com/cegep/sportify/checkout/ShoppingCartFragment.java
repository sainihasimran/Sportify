package com.cegep.sportify.checkout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.R;
import com.cegep.sportify.SportifyApp;
import com.cegep.sportify.Utils;
import com.cegep.sportify.checkout.adapter.ShoppingCartAdapter;
import com.cegep.sportify.model.Equipment;
import com.cegep.sportify.model.Order;
import com.cegep.sportify.model.Product;
import com.cegep.sportify.model.ShoppingCartItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShoppingCartFragment extends Fragment implements ShoppingCartChangeListener {

    private View totalPriceContainer;

    private TextView totalPriceTextView;

    private RecyclerView recyclerView;

    private View contentContainer;

    private View emptyContainer;

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

        contentContainer = view.findViewById(R.id.content_container);
        emptyContainer = view.findViewById(R.id.empty_container);

        totalPriceContainer = view.findViewById(R.id.total_price_container);

        totalPriceTextView = view.findViewById(R.id.total_price_text);
        setupRecyclerView(view);

        progress = new AlertDialog.Builder(requireContext(), R.style.LoadingDialogStyle)
                .setView(R.layout.item_loading)
                .setCancelable(false)
                .create();

        progress.show();

        Utils.getShoppingCartReference().addListenerForSingleValueEvent(valueEventListener);

        setupCheckoutButton(view);
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
    }

    private void setupCheckoutButton(View view) {
        view.findViewById(R.id.checkout_button).setOnClickListener(v -> {
            if (shoppingCartAdapter != null && shoppingCartAdapter.getShoppingCartItems() != null && !shoppingCartAdapter.getShoppingCartItems()
                    .isEmpty()) {
                List<Order> orders = new ArrayList<>();
                for (ShoppingCartItem shoppingCartItem : shoppingCartAdapter.getShoppingCartItems()) {
                    if ((shoppingCartItem.getProduct() != null && !shoppingCartItem.getProduct().isOutOfStock()) || (
                            shoppingCartItem.getEquipment() != null && !shoppingCartItem.getEquipment().isOutOfStock())) {
                        orders.add(shoppingCartItem.toOrder());
                    }
                }

                if (orders.isEmpty()) {
                    Toast.makeText(requireActivity(), "No products to order", Toast.LENGTH_SHORT).show();
                    return;
                }

                SportifyApp.orders.clear();
                SportifyApp.orders.addAll(orders);
                SportifyApp.isBuyMode = false;

                Intent intent = ShippingActivity.getCallingIntent(requireContext(), true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onShoppingCartChanged(ShoppingCartItem shoppingCartItem, boolean fromDelete) {
        updateTotalPrice();
        shoppingCartAdapter.refresh();
        if (!fromDelete) {
            Utils.getShoppingCartReference().child(shoppingCartItem.getCartId()).setValue(shoppingCartItem);
        }
    }

    @Override
    public void onShoppingCartItemDeleted(ShoppingCartItem shoppingCartItem) {
        Utils.getShoppingCartReference().child(shoppingCartItem.getCartId()).removeValue();
        shoppingCartAdapter.handleItemDeleted(shoppingCartItem);

        updateContentVisibility();

        Toast.makeText(requireContext(), "Item removed from shopping cart", Toast.LENGTH_SHORT).show();
    }

    private void showShoppingCartItemsList() {
        shoppingCartAdapter = new ShoppingCartAdapter(requireContext(), shoppingCartItems, this);
        recyclerView.setAdapter(shoppingCartAdapter);
        updateTotalPrice();

        updateContentVisibility();
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
                Iterator<ShoppingCartItem> iterator = shoppingCartItems.iterator();
                while (iterator.hasNext()) {
                    ShoppingCartItem shoppingCartItem = iterator.next();
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

                    if (shoppingCartItem.getProduct() == null && shoppingCartItem.getEquipment() == null) {
                        iterator.remove();
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

    private void updateContentVisibility() {
        if (shoppingCartAdapter.getShoppingCartItems() == null || shoppingCartAdapter.getShoppingCartItems().isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
        }
    }
}
