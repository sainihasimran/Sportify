package com.cegep.sportify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.mapbox.search.result.SearchSuggestion;
import java.util.ArrayList;
import java.util.List;

public class SearchAddressAdapter extends RecyclerView.Adapter<SearchAddressAdapter.SearchAddressViewHolder> {

    private final List<SearchSuggestion> searchSuggestions = new ArrayList<>();

    @NonNull
    @Override
    public SearchAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_address, parent, false);
        return new SearchAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAddressViewHolder holder, int position) {
        holder.bind(searchSuggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return searchSuggestions.size();
    }

    public void updateList(List<? extends SearchSuggestion> searchSuggestions) {
        this.searchSuggestions.clear();
        this.searchSuggestions.addAll(searchSuggestions);
        notifyDataSetChanged();
    }

    static class SearchAddressViewHolder extends RecyclerView.ViewHolder {

        private final TextView addressLine1TextView;

        private final TextView addressLine2TextView;

        private final TextView postalCodeTextView;

        public SearchAddressViewHolder(@NonNull View itemView) {
            super(itemView);

            addressLine1TextView = itemView.findViewById(R.id.address_line_1);
            addressLine2TextView = itemView.findViewById(R.id.address_line_2);
            postalCodeTextView = itemView.findViewById(R.id.postal_code);
        }

        public void bind(SearchSuggestion searchSuggestion) {
            addressLine1TextView.setText(Utils.getAddressLine1(searchSuggestion));
            addressLine2TextView.setText(Utils.getAddressLine2(searchSuggestion));
            postalCodeTextView.setText(Utils.getPostalCode(searchSuggestion));
        }
    }
}
