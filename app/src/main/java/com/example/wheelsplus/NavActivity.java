package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    GroupFragment groupFragment = new GroupFragment();
    ChatFragment chatFragment = new ChatFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nav);

        replaceFragment(homeFragment);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        if(savedInstanceState != null){
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            groupFragment = (GroupFragment) getSupportFragmentManager().getFragment(savedInstanceState, "groupFragment");
            chatFragment = (ChatFragment) getSupportFragmentManager().getFragment(savedInstanceState, "chatFragment");
            settingsFragment = (SettingsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "settingsFragment");
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.group:
                        replaceFragment(groupFragment);
                        break;
                    case R.id.chat:
                        replaceFragment(chatFragment);
                        break;
                    case R.id.settings:
                        replaceFragment(settingsFragment);
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(homeFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment);
        }
        if(groupFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "groupFragment", groupFragment);
        }
        if(chatFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "chatFragment", chatFragment);
        }
        if(settingsFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "settingsFragment", settingsFragment);
        }
    }

}