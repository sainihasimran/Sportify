package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.AddressSelectedListener;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.model.Address;
import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private final List<Address> addressList = new ArrayList<>();

    private final AddressSelectedListener addressSelectedListener;

    public AddressAdapter(AddressSelectedListener addressSelectedListener) {
        this.addressSelectedListener = addressSelectedListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view, addressSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.bind(addressList.get(position));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void updateList(List<Address> addressList) {
        this.addressList.clear();
        this.addressList.addAll(addressList);
        notifyDataSetChanged();
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;

        private final TextView addressLine1TextView;

        private final TextView addressLine2TextView;

        private final TextView postalCodeTextView;

        private final TextView phoneTextView;

        private final ImageView selectedIcon;

        private Address address;

        public AddressViewHolder(@NonNull View itemView, AddressSelectedListener addressSelectedListener) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.full_name_text);
            addressLine1TextView = itemView.findViewById(R.id.address_line_1);
            addressLine2TextView = itemView.findViewById(R.id.address_line_2);
            postalCodeTextView = itemView.findViewById(R.id.postal_code_text);
            phoneTextView = itemView.findViewById(R.id.phone_text);
            selectedIcon = itemView.findViewById(R.id.selected_icon);

            itemView.setOnClickListener(v -> addressSelectedListener.onAddressSelected(address));
            itemView.findViewById(R.id.edit_button).setOnClickListener(v -> addressSelectedListener.onEditAddressClicked(address));
        }

        public void bind(Address address) {
            this.address = address;

            nameTextView.setText(address.getName());
            addressLine1TextView.setText(Utils.getAddressLine1(address));
            addressLine2TextView.setText(Utils.getAddressLine2(address));
            postalCodeTextView.setText(address.getPostalCode());
            phoneTextView.setText(address.getPhoneNumber());
            selectedIcon.setVisibility(address.isSelected() ? View.VISIBLE : View.GONE);
        }
    }
}
