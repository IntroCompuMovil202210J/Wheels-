package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DriverNavActivity extends AppCompatActivity {

    BottomNavigationView driverBottomNavigationView;

    DriverHomeFragment driverHomeFragment = new DriverHomeFragment();
    DriverGroupFragment driverGroupFragment = new DriverGroupFragment();
    DisplayChatsFragment displayChatsFragment = new DisplayChatsFragment();
    DriverSettingsFragment driverSettingsFragment = new DriverSettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_driver_nav);

        replaceFragment(driverHomeFragment);

        driverBottomNavigationView = findViewById(R.id.driver_bottom_nav);

        if(savedInstanceState != null){
            driverHomeFragment = (DriverHomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "driverHomeFragment");
            driverGroupFragment = (DriverGroupFragment) getSupportFragmentManager().getFragment(savedInstanceState, "driverGroupFragment");
            displayChatsFragment = (DisplayChatsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "displayChatsFragment");
            driverSettingsFragment = (DriverSettingsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "driverSettingsFragment");
        }

        driverBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        replaceFragment(driverHomeFragment);
                        break;
                    case R.id.group:
                        replaceFragment(driverGroupFragment);
                        break;
                    case R.id.chat:
                        replaceFragment(displayChatsFragment);
                        break;
                    case R.id.settings:
                        replaceFragment(driverSettingsFragment);
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(driverHomeFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "driverHomeFragment", driverHomeFragment);
        }
        if(driverGroupFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "driverGroupFragment", driverGroupFragment);
        }
        if(displayChatsFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "displayChatsFragment", displayChatsFragment);
        }
        if(driverSettingsFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "driverSettingsFragment", driverSettingsFragment);
        }
    }

}