package com.example.pam_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddBucketActivity;
import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.listener.Clickable;
import com.example.pam_app.view.BucketListView;
import com.example.pam_app.view.HomeView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements Clickable {

    private static final int HOME_VIEW = 0;
    private static final int BUCKETS_VIEW = 1;

    private BottomNavigationView navigationView;
    private ViewSwitcher viewSwitcher;
    private HomeView homeView;
    private BucketListView bucketListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    viewSwitcher.setDisplayedChild(BUCKETS_VIEW);
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
        setUpBucketListView();
    }

    private void setUpHomeView() {
        homeView = findViewById(R.id.home);
        homeView.bind();
        homeView.setClickable(this);
    }

    private void setUpBucketListView() {
        bucketListView = findViewById(R.id.buckets);
        bucketListView.bind(this, this::launchAddBucketActivity, this::launchBucketDetailActivity);
    }

    public void addEntry(final View view) {
        final Intent intent = new Intent(this, AddBucketEntryActivity.class);
        startActivity(intent);
    }

    public void launchBucketDetailActivity(int bucketId) {
        String uri = "wally://bucket/detail?id=" + bucketId;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void launchAddBucketActivity() {
        final Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick() {
        final Intent intent = new Intent(this, AddBucketActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        homeView.onViewStopped();
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeView.onViewResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeView.onViewPaused();

    }
}