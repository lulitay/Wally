package com.example.pam_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddBucketActivity;
import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.listener.Clickable;
import com.example.pam_app.view.BucketListView;
import com.example.pam_app.view.HomeView;
import com.example.pam_app.view.ProfileView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Clickable {

    public static final String KEY_PREF_LANGUAGE = "pref_language";

    private static final int HOME_VIEW = 0;
    private static final int BUCKETS_VIEW = 1;
    private static final int PROFILE_VIEW = 2;

    private BottomNavigationView navigationView;
    private ViewFlipper viewFlipper;

    private HomeView homeView;
    private BucketListView bucketListView;
    private ProfileView profileView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSharedPreferencesListener(sharedPreferences);
        setUpChosenLanguage();

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
                    viewFlipper.setDisplayedChild(HOME_VIEW);
                    return true;
                case R.id.buckets:
                    viewFlipper.setDisplayedChild(BUCKETS_VIEW);
                    return true;
                case R.id.profile:
                    viewFlipper.setDisplayedChild(PROFILE_VIEW);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setUpViews() {
        viewFlipper = findViewById(R.id.switcher);
        setUpHomeView();
        setUpBucketListView();
        setUpProfileView();
    }

    private void setUpHomeView() {
        homeView = findViewById(R.id.home);
        homeView.bind();
    }

    private void setUpBucketListView() {
        bucketListView = findViewById(R.id.buckets);
        bucketListView.bind(this, this::launchAddBucketActivity, this::launchBucketDetailActivity);
    }

    private void setUpProfileView() {
        profileView = findViewById(R.id.profile);
        profileView.bind(sharedPreferences);
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

    private void setSharedPreferencesListener(final SharedPreferences sharedPreferences) {
        final SharedPreferences.OnSharedPreferenceChangeListener listener =
                (sharedPreferences1, key) -> {
                    if (key.equals(KEY_PREF_LANGUAGE)) {
                        String languagePref_ID = sharedPreferences1.getString(KEY_PREF_LANGUAGE, "");
                        switch (languagePref_ID) {
                            case "en":
                                Locale localeEN = new Locale("en");
                                updateLocale(localeEN);
                                break;
                            case "es":
                                Locale localeES = new Locale("es");
                                updateLocale(localeES);
                                break;

                        }
                    }
                };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void updateLocale(final Locale locale) {
        setLocale(locale);
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setLocale(final Locale locale) {
        Locale.setDefault(locale);
        final Resources res = getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    private void setUpChosenLanguage() {
        final Locale locale = new Locale(sharedPreferences.getString(KEY_PREF_LANGUAGE, "en"));
        setLocale(locale);
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        setUpViews();
        super.onConfigurationChanged(newConfig);
    }
}