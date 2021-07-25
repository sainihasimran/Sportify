package com.cegep.sportify;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.Adapter.SearchAddressAdapter;
import com.mapbox.search.Country;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SearchAddressFragment extends Fragment {

    private SearchEngine searchEngine;

    private SearchRequestTask searchRequestTask;

    private SearchAddressAdapter adapter;

    private View emptyContainer;

    private boolean isSearchFieldEmpty;

    private final SearchOptions searchOptions = new SearchOptions.Builder()
            .limit(5)
            .countries(Country.CANADA)
            .requestDebounce(500)
            .build();

    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {
        @Override
        public void onResult(@NotNull SearchSuggestion searchSuggestion, @NotNull SearchResult searchResult, @NotNull ResponseInfo responseInfo) {

        }

        @Override
        public void onCategoryResult(@NotNull SearchSuggestion searchSuggestion, @NotNull List<? extends SearchResult> list,
                                     @NotNull ResponseInfo responseInfo) {

        }

        @Override
        public void onSuggestions(@NotNull List<? extends SearchSuggestion> suggestions, @NotNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                if (isSearchFieldEmpty) {
                    emptyContainer.setVisibility(View.GONE);
                } else {
                    emptyContainer.setVisibility(View.VISIBLE);
                }

                adapter.updateList(Collections.emptyList());
            } else {
                adapter.updateList(suggestions);
            }
        }

        @Override
        public void onError(@NotNull Exception e) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchEngine = MapboxSearchSdk.createSearchEngine();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SearchAddressAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        EditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSearchFieldEmpty = TextUtils.isEmpty(s);
                searchRequestTask = searchEngine.search(s.toString(), searchOptions, searchCallback);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emptyContainer = view.findViewById(R.id.empty_container);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (searchRequestTask != null) {
            searchRequestTask.cancel();
        }
    }
}
