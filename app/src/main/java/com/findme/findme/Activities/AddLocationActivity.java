package com.findme.findme.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.findme.findme.AsyncTasks.FetchAddressTask;
import com.findme.findme.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddLocationActivity extends BaseActivity implements FetchAddressTask.OnTaskCompleted {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "ADD_LOCATION_ACTIVITY";
    private EditText mAddress, mName;
    private Button save;
    private Location mLocation;
    private Double longitude,latitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private String address = "";

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add");
        setContentView(R.layout.activity_add_location);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAddress = findViewById(R.id.addressText);
        mName = findViewById(R.id.nameText);
        save = findViewById(R.id.saveBtn);
        save.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add = mAddress.getText().toString();
                String name = mName.getText().toString();
                if(add.isEmpty()){
                    mAddress.setError("Required");
                    mAddress.requestFocus();
                    return;
                }if(name.isEmpty()){
                    mName.setError("Required");
                    mName.requestFocus();
                    return;
                }

                Map<String, Object> l= new HashMap<>();
                l.put("name", name);
                l.put("address", address);
                l.put("latitude", latitude);
                l.put("longitude", longitude);


                // Add a new document with a generated ID
                db.collection("Locations")
                        .add(l)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Snackbar.make(findViewById(android.R.id.content),"Your Location " +
                                        "Added Successfully",Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Snackbar.make(findViewById(android.R.id.content),"Something Went " +
                                        "Wrong. Try Again",Snackbar.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                finish();
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        mAddress.setText(getString(R.string.loading));
    }


    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        } else {
            Log.d(TAG, "getLocation: permissions granted");
        }
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            new FetchAddressTask(AddLocationActivity.this, AddLocationActivity.this).execute(location);
                        }
                    }
                });

            }
        }).start();
        hideProgressDialog();
        save.setEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Snackbar.make(findViewById(android.R.id.content),"Please Grant Permission to " +
                            "access Location",Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onTaskCompleted(String result) {
        mAddress.setText(getString(R.string.address_text, result));
        address = result;
    }
}
