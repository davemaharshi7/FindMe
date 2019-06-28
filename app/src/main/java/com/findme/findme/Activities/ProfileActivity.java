package com.findme.findme.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.findme.findme.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePhoto;
    private TextView name,email,phone,dob;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);
        profilePhoto = findViewById(R.id.profileImage);
        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userPhone);
        dob = findViewById(R.id.userDob);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Glide.with(this).load(user.getPhotoUrl()).into(profilePhoto);
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
//        phone.setText(user.getProviderId());
//
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        dob.setText(acct.getFamilyName());



    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if(user==null){
            Snackbar.make(findViewById(android.R.id.content),"You Are Not Logged In",
                    Snackbar.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(),LandingActivity.class);
            startActivity(i);
            finish();
        }
    }
}
