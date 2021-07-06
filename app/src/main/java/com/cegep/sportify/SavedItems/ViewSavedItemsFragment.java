package com.cegep.sportify.SavedItems;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cegep.sportify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ViewSavedItemsFragment extends Fragment {

    private ArrayList<SavedItems> saveditems;
    RecyclerView recyclerview;
    saveditemadapter adapter;
    DatabaseReference dbr;

    public ViewSavedItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_saved_items, container, false);

        recyclerview = view.findViewById(R.id.recyclerView);

        dbr = FirebaseDatabase.getInstance().getReference("favorites");

        saveditems = new ArrayList<>();

        adapter = new saveditemadapter(getContext(),saveditems);

        recyclerview.setAdapter(adapter);
        recyclerview.setVisibility(View.VISIBLE);
        return view;
    }




}