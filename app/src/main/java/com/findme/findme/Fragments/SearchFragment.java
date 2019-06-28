package com.findme.findme.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findme.findme.Model.LocationModel;
import com.findme.findme.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private View rootView;
    private Context con;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore rootRef;
    private FirestoreRecyclerAdapter<LocationModel, LocationViewHolder> adapter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initUIElements();
        return rootView;
    }

    private void initUIElements() {
        con = getContext();
        linearLayoutManager = new LinearLayoutManager(con);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Locations")
                .orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<LocationModel> options = new FirestoreRecyclerOptions.Builder<LocationModel>()
                .setQuery(query, LocationModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<LocationModel, LocationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull LocationModel model) {
                holder.setName(model.getName());
                holder.setAddress(model.getAddress());
                holder.setcDelimeter("Kms Away");
                holder.setKM(String.valueOf(model.getLatitude()));

            }

            @NonNull
            @Override
            public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent,
                        false);
                return new LocationViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
    private class LocationViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView cname,cAdd,cKm,cDelimeter;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            cname = itemView.findViewById(R.id.cardName);
            cAdd = itemView.findViewById(R.id.cardAddress);
            cKm= itemView.findViewById(R.id.kmCard);
            cDelimeter = itemView.findViewById(R.id.delimeterCard);

        }

        void setName(String productName) {
            cname.setText(productName);
        }
        void setAddress(String productName) {
            cAdd.setText(productName);
        }void setKM(String productName) {
            cKm.setText(productName);
        }void setcDelimeter(String productName) {
            cDelimeter.setText(productName);
        }
    }

}
