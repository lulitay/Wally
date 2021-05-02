package com.example.pam_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddBucketActivity;
import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.activity.FTUActivity;
import com.example.pam_app.listener.OnAddBucketClickedListener;
import com.example.pam_app.view.HomeView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnAddBucketClickedListener {

    private static final String SP_ID = "demo-pref";
    private static final String FTU = "ftu";
    private static final int HOME_VIEW = 0;
    private BottomNavigationView navigationView;
    private ViewSwitcher viewSwitcher;
    private HomeView homeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(SP_ID, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FTU, true)) {
            sharedPreferences.edit().putBoolean(FTU, false).apply();
            startActivity(new Intent(this, FTUActivity.class));
        }
        setUpViews();
        setUpBottomNavigation();

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addEntry);
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpBottomNavigation() {
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.home);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    viewSwitcher.setDisplayedChild(HOME_VIEW);
                    return true;
                case R.id.buckets:
                    return true;
                case R.id.profile:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setUpViews() {
        viewSwitcher = findViewById(R.id.switcher);
        setUpHomeView();
    }

    private void setUpHomeView() {
        homeView = findViewById(R.id.home);
        homeView.bind();
        homeView.setOnAddBucketClickedListener(this);
    }

    public void addEntry(final View view) {
        final Intent intent = new Intent(this, AddBucketEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick() {
        final Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }
}