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

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pam_app.activity.AddBucketEntryActivity;
import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.presenter.MainPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.utils.contracts.BucketContract;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketListView;
import com.example.pam_app.view.HomeView;
import com.example.pam_app.view.MainView;
import com.example.pam_app.view.ProfileView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Clickable, MainView {

    public static final String KEY_PREF_LANGUAGE = "pref_language";

    private static final int HOME_VIEW = 0;
    private static final int BUCKETS_VIEW = 1;
    private static final int PROFILE_VIEW = 2;

    private BottomNavigationView navigationView;
    private ViewFlipper viewFlipper;

    private HomeView homeView;
    private BucketListView bucketListView;
    private ProfileView profileView;

    private ActivityResultLauncher<String> addBucketResultLauncher;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(this.getApplicationContext()).bucketDao(),
                new BucketMapper()
        );

        final SchedulerProvider provider = new AndroidSchedulerProvider();
        presenter = new MainPresenter(bucketRepository, this, provider);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setSharedPreferencesListener(sharedPreferences);
        setUpChosenLanguage();

        setContentView(R.layout.activity_main);

        setUpViews();
        setUpBottomNavigation();
        setUpActivityResultLauncher();

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::addEntry);
    }

    @Override
    public void onClick() {
        addBucketResultLauncher.launch("addBucket");
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewStop();
        homeView.onViewStopped();
        bucketListView.onViewStop();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeView.onViewResumed();
        bucketListView.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeView.onViewPaused();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
        homeView.bind();
        profileView.bind(sharedPreferences);

    }

    @Override
    public void onBucketListViewReceived(final List<Bucket> bucketList) {
        bucketListView.bind(this, this::launchAddBucketActivity, this::launchBucketDetailActivity, bucketList);
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
        homeView = findViewById(R.id.home);
        bucketListView = findViewById(R.id.buckets);
        profileView = findViewById(R.id.profile);
    }


    private void addEntry(final View view) {
        final Intent intent = new Intent(this, AddBucketEntryActivity.class);
        startActivity(intent);
    }

    private void launchBucketDetailActivity(int bucketId) {
        final String uri = "wally://bucket/detail?id=" + bucketId;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void launchAddBucketActivity() {
        addBucketResultLauncher.launch("addBucket");
    }

    private void setSharedPreferencesListener(final SharedPreferences sharedPreferences) {
        this.onSharedPreferenceChangeListener =
            (sp, key) -> {
                if (key.equals(KEY_PREF_LANGUAGE)) {
                    final String languagePref = sp.getString(KEY_PREF_LANGUAGE, "");
                    switch (languagePref) {
                        case "en":
                            Locale localeEN = new Locale("en");
                            MainActivity.this.updateLocale(localeEN);
                            break;
                        case "es":
                            Locale localeES = new Locale("es");
                            MainActivity.this.updateLocale(localeES);
                            break;

                    }
                }
            };
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private void updateLocale(final Locale locale) {
        setLocale(locale);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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

    private void setUpActivityResultLauncher() {
        addBucketResultLauncher = registerForActivityResult(
                new BucketContract(),
                result -> bucketListView.onBucketAdded(result)
        );
    }
}