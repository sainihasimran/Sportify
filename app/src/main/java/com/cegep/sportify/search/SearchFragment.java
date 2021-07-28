package com.cegep.sportify.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cegep.sportify.R;
import com.cegep.sportify.Utils;
import com.cegep.sportify.search.Adapter.SearchItemAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchFragment extends Fragment {

    private SearchItemAdapter searchItemAdapter;

    private View emptyView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyView = view.findViewById(R.id.empty_view);
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(searchItemAdapter);

    }
}