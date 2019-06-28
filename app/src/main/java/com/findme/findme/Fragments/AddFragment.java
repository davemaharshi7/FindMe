    package com.findme.findme.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.findme.findme.Activities.AddLocationActivity;
import com.findme.findme.Listeners.FragmentActionListener;
import com.findme.findme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    private TextView currentLocation;
    private Context context;
    private View rootView;
    private FragmentActionListener fragmentActionListener;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Location");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add, container, false);
        initUI();
        return rootView;
    }

    public void setActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    private void initUI() {
        context = getContext();
        currentLocation = rootView.findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentActionListener!=null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY,FragmentActionListener.ACTION_VALUE_CURRENT_LOCATION);
                    fragmentActionListener.actionPerformed(bundle);
                }
            }
        });
    }

}
