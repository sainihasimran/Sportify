package com.cegep.sportify;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.sportify.Adapter.SearchAddressAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Point;
import com.mapbox.search.Country;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.ReverseGeoOptions;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SearchAddressFragment extends Fragment implements SearchAddressItemClickListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 219;

    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    public static final String KEY_SELECTED_ADDRESS = "KEY_SELECTED_ADDRESS";

    private SearchEngine searchEngine;

    private SearchRequestTask searchRequestTask;

    private SearchAddressAdapter adapter;

    private View emptyContainer;

    private boolean isSearchFieldEmpty;

    private FusedLocationProviderClient fusedLocationProviderClient;

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
                emptyContainer.setVisibility(View.GONE);
                adapter.updateList(suggestions);
            }
        }

        @Override
        public void onError(@NotNull Exception e) {
            e.printStackTrace();
            Context context = getContext();
            if (context == null) {
                return;
            }

            Toast.makeText(context, "Failed to find addresses", Toast.LENGTH_SHORT).show();
        }
    };
    private AlertDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchEngine = MapboxSearchSdk.createSearchEngine();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkLocationPermission();

        adapter = new SearchAddressAdapter(this);
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
        setupCurrentLocation(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (searchRequestTask != null) {
            searchRequestTask.cancel();
        }
    }

    @Override
    public void onSearchAddressSelected(SearchSuggestion searchSuggestion) {
        setAddressResult(searchSuggestion.getAddress());
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }

                Toast.makeText(activity, "Location permission denied", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }
    }

    private void setupCurrentLocation(View view) {
        view.findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = new AlertDialog.Builder(requireContext(), R.style.LoadingDialogStyle)
                        .setView(R.layout.item_loading)
                        .setCancelable(false)
                        .show();
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location == null) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }

                        Activity activity = getActivity();
                        if (activity != null) {
                            Toast.makeText(activity, "No last location found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        reverseGeocode(location);
                    }
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    Activity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void reverseGeocode(Location location) {
        final ReverseGeoOptions reverseGeoOptions = new ReverseGeoOptions.Builder(Point.fromLngLat(location.getLongitude(), location.getLatitude()))
                .limit(1)
                .build();

        MapboxSearchSdk.createReverseGeocodingSearchEngine().search(reverseGeoOptions, new SearchCallback() {
            @Override
            public void onResults(@NotNull List<? extends SearchResult> list, @NotNull ResponseInfo responseInfo) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }

                if (list.isEmpty()) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setAddressResult(list.get(0).getAddress());
                }
            }

            @Override
            public void onError(@NotNull Exception e) {
                e.printStackTrace();
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }

                Activity activity = getActivity();
                if (activity != null) {
                    Toast.makeText(activity, "Failed to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAddressResult(SearchAddress searchAddress) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Intent data = new Intent();
        data.putExtra(KEY_SELECTED_ADDRESS, searchAddress);
        activity.setResult(Activity.RESULT_OK, data);
        activity.finish();
    }
}
