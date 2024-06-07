package com.example.runningapplication.runningMain;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amap.api.services.core.ServiceSettings;
import com.example.runningapplication.R;
import com.example.runningapplication.databinding.ActivityRunningMainBinding;
import com.example.runningapplication.service.chatSocketService;
import com.example.runningapplication.service.locationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class runningMainActivity extends AppCompatActivity {

    private ActivityRunningMainBinding binding;

    public static runningMainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this,true);
        Intent intent=new Intent(runningMainActivity.this, locationService.class);
        startService(intent);

        instance = this;
        binding = ActivityRunningMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        setSupportActionBar(findViewById(R.id.navigatek_toolbar));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_calendar,
                R.id.navigation_friend_list,
                R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_running_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, chatSocketService.class);
        stopService(intent);
    }
}