package com.findme.findme.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.findme.findme.Activities.LandingActivity;
import com.findme.findme.Fragments.AddFragment;
import com.findme.findme.Fragments.SearchFragment;
import com.findme.findme.Listeners.FragmentActionListener;
import com.findme.findme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements FragmentActionListener {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if(user == null){
            Intent back = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(back);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_buttons,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.profile_btn){
//            Toast.makeText(getApplicationContext(),"PROFILE",Toast.LENGTH_SHORT).show();
            Intent profile = new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(profile);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragmentManager = getSupportFragmentManager();

        initializeAddFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_add:
                        bottomNavigationView.setSelected(true);
                        initializeAddFragment();
                        return true;
                    case R.id.nav_search:
                        bottomNavigationView.setSelected(true);
                        initializeSearchFragment();
                        return true;
                    case R.id.nav_logout:
                        customDialog("Logout","Are you Sure you want to Logout","cancelMethod","logout");
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    private void initializeSearchFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        //searchFragment.setActionListener(this);
        fragmentTransaction.replace(R.id.frameLayout,searchFragment).commit();

    }

    private void initializeAddFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        AddFragment addFragment = new AddFragment();
        addFragment.setActionListener(this);
        fragmentTransaction.replace(R.id.frameLayout,addFragment).commit();
    }

    public void customDialog(String title, String message, final String cancelMethod, final String okMethod){
        final android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);

        builderSingle.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("DIALOG", "onClick: Cancel Called.");
                        if(cancelMethod.equals("cancelMethod")){
//                            cancelMethod1();
                        }

                    }
                });

        builderSingle.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("DIALOG", "onClick: OK Called.");
                        if(okMethod.equals("logout")){
                            logoutUser();
                        }
                        
                    }
                });


        builderSingle.show();
    }

    private void logoutUser() {
        mAuth.signOut();
        updateUI(null);
    }

    @Override
    public void actionPerformed(Bundle bundle) {
        int action = bundle.getInt(FragmentActionListener.ACTION_KEY);
        switch (action){
            case FragmentActionListener.ACTION_VALUE_CURRENT_LOCATION:
                //Invoke Activity as per requirement
                Intent i = new Intent(getApplicationContext(),AddLocationActivity.class);
                startActivity(i);
        }
    }
}
