package com.example.pam_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.pam_app.di.Container;
import com.example.pam_app.di.ContainerLocator;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;
import com.example.pam_app.presenter.MainPresenter;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.contracts.BucketContract;
import com.example.pam_app.utils.contracts.EntryContract;
import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.workers.RecurrentBucketWorker;
import com.example.pam_app.utils.workers.RecurrentBucketWorkerFactory;
import com.example.pam_app.view.BucketListView;
import com.example.pam_app.view.HomeView;
import com.example.pam_app.view.IncomeView;
import com.example.pam_app.view.MainView;
import com.example.pam_app.view.ProfileView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.DAY_OF_MONTH;

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
        final Container container = ContainerLocator.locateComponent(this);
        languagesRepository = container.getLanguageRepository(); //TODO change
        presenter = new MainPresenter(container.getBucketRepository(), container.getIncomeRepository(),
                this, container.getSchedulerProvider(), languagesRepository);
        setContentView(R.layout.activity_main);

        setUpChosenLanguage();
        setUpViews();
        setUpBottomNavigation();
        setUpAddBucketResultLauncher();
        setUpAddEntryResultLauncher();
        setUpFAB();
        setUpRecurrentBucketWorker();
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

    private void setUpRecurrentBucketWorker() {
        final Container container = ContainerLocator.locateComponent(this);
        final androidx.work.Configuration myConfig = new androidx.work.Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .setWorkerFactory(new RecurrentBucketWorkerFactory(container.getBucketRepository()))
                .build();
        WorkManager.initialize(this, myConfig);

        final WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        final Calendar currentDate = Calendar.getInstance();
        final Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR_OF_DAY, 1);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        dueDate.set(Calendar.DAY_OF_MONTH, currentDate.get(DAY_OF_MONTH) + 1);

        final long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
        final PeriodicWorkRequest bucketRecurrent =
                new PeriodicWorkRequest.Builder(RecurrentBucketWorker.class,
                        1, TimeUnit.DAYS,
                        30, TimeUnit.MINUTES)
                        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                        .build();
        workManager.enqueueUniquePeriodicWork("recurrent_bucket",
                ExistingPeriodicWorkPolicy.REPLACE, bucketRecurrent);
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