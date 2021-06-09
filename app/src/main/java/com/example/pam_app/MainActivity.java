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

import com.example.pam_app.db.WallyDatabase;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;
import com.example.pam_app.presenter.MainPresenter;
import com.example.pam_app.repository.BucketMapper;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeMapper;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.repository.LanguagesRepositoryImpl;
import com.example.pam_app.repository.RoomBucketRepository;
import com.example.pam_app.repository.RoomIncomeRepository;
import com.example.pam_app.utils.contracts.BucketContract;
import com.example.pam_app.utils.contracts.EntryContract;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketListView;
import com.example.pam_app.view.HomeView;
import com.example.pam_app.view.IncomeView;
import com.example.pam_app.view.MainView;
import com.example.pam_app.view.ProfileView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Clickable, MainView {

    private static final int HOME_VIEW = 0;
    private static final int BUCKETS_VIEW = 1;
    private static final int INCOME_VIEW = 2;
    private static final int PROFILE_VIEW = 3;

    private BottomNavigationView navigationView;
    private ViewFlipper viewFlipper;

    private HomeView homeView;
    private BucketListView bucketListView;
    private IncomeView incomeView;
    private ProfileView profileView;

    private ActivityResultLauncher<String> addBucketResultLauncher;
    private ActivityResultLauncher<String> addBucketEntryResultLauncher;

    private MainPresenter presenter;
    private LanguagesRepository languagesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BucketRepository bucketRepository = new RoomBucketRepository(
                WallyDatabase.getInstance(this.getApplicationContext()).bucketDao(),
                new BucketMapper()
        );
        final IncomeRepository incomeRepository = new RoomIncomeRepository(
                WallyDatabase.getInstance(this.getApplicationContext()).incomeDao(),
                new IncomeMapper()
        );
        final SchedulerProvider provider = new AndroidSchedulerProvider();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        languagesRepository = new LanguagesRepositoryImpl(sharedPreferences);
        presenter = new MainPresenter(bucketRepository, incomeRepository, this, provider, languagesRepository);
        setContentView(R.layout.activity_main);

        setUpChosenLanguage();
        setUpViews();
        setUpBottomNavigation();
        setUpAddBucketResultLauncher();
        setUpAddEntryResultLauncher();
        setUpFAB();
    }

    @Override
    public void onClick() {
        addBucketResultLauncher.launch("addBucket");
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewStop();
        languagesRepository.unregisterOnSharedPreferencesListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached();
        profileView.bind(languagesRepository, this::applyChanges);
    }

    @Override
    public void onBucketListViewReceived(final List<Bucket> bucketList) {
        bucketListView.bind(this, this::launchAddBucketActivity, this::launchBucketDetailActivity, bucketList);
    }

    @Override
    public void onEntriesReceived(final List<BucketEntry> entryList) {
        homeView.bind(entryList);
    }

    @Override
    public void onIncomeDataReceived(final List<Income> incomeList, final Double incomeLeft) {
        incomeView.bind(incomeList, incomeLeft);
    }

    @Override
    public void updateLocale(final Locale locale) {
        setLocale(locale);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                case R.id.income:
                    viewFlipper.setDisplayedChild(INCOME_VIEW);
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
        incomeView = findViewById(R.id.income);
        profileView = findViewById(R.id.profile);
    }

    private void launchAddBucketEntryActivity(final View view) {
        addBucketEntryResultLauncher.launch("addEntry");
    }

    private void launchBucketDetailActivity(Integer bucketId) {
        final String uri = "wally://bucket/detail?id=" + bucketId;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void launchAddBucketActivity() {
        addBucketResultLauncher.launch("addBucket");
    }

    private void setUpFAB() {
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::launchAddBucketEntryActivity);
    }

    private void setLocale(final Locale locale) {
        Locale.setDefault(locale);
        final Resources res = getResources();
        final DisplayMetrics dm = res.getDisplayMetrics();
        final Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        res.updateConfiguration(conf, dm);
    }

    private void onEntryAdded(final Serializable entry) {
        if (entry instanceof BucketEntry) {
            homeView.onBucketEntryAdded((BucketEntry) entry);
            incomeView.onBucketEntryAdded(((BucketEntry) entry).getAmount());
        } else {
            incomeView.onIncomeAdded((Income) entry);
        }
    }

    private void setUpChosenLanguage() {
        setLocale(languagesRepository.getCurrentLocale());
    }

    private void setUpAddBucketResultLauncher() {
        addBucketResultLauncher = registerForActivityResult(
                new BucketContract(),
                result -> bucketListView.onBucketAdded(result)
        );
    }

    private void setUpAddEntryResultLauncher() {
        addBucketEntryResultLauncher = registerForActivityResult(
                new EntryContract(),
                this::onEntryAdded
        );
    }

    private void applyChanges(String language) {
        languagesRepository.changeLanguage(language);
    }
}