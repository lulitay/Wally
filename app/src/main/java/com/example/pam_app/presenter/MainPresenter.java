package com.example.pam_app.presenter;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.IncomeRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.BucketEntryComparator;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.MainView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.example.pam_app.repository.LanguagesRepositoryImpl.KEY_PREF_LANGUAGE;

public class MainPresenter {

    private final WeakReference<MainView> mainView;
    private final BucketRepository bucketRepository;
    private final IncomeRepository incomeRepository;
    private final SchedulerProvider schedulerProvider;
    private final LanguagesRepository languagesRepository;
    private CompositeDisposable disposable;

    private Double totalIncome;
    private Double totalSpending;

    private List<Bucket> bucketList;
    private List<BucketEntry> bucketEntryList;
    private List<Income> incomeList;

    public MainPresenter(
            final BucketRepository bucketRepository,
            final IncomeRepository incomeRepository,
            final MainView mainActivity,
            final SchedulerProvider schedulerProvider,
            final LanguagesRepository languagesRepository
    ) {
        this.mainView = new WeakReference<>(mainActivity);
        this.bucketRepository = bucketRepository;
        this.incomeRepository = incomeRepository;
        this.schedulerProvider = schedulerProvider;
        this.languagesRepository = languagesRepository;
        this.totalIncome = null;
        this.totalSpending = null;
        this.bucketList = null;
        this.bucketEntryList = null;
        this.incomeList = null;
    }

    public void onViewAttached() {
        this.disposable = new CompositeDisposable();
        languagesRepository.setOnSharedPreferencesListener(sharedPreferencesListener());

        if (bucketList == null) {
            disposable.add(bucketRepository.getList()
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onBucketsReceived));
        }

        if (bucketEntryList == null) {
            disposable.add(bucketRepository.getEntryList()
                    .map(unsortedList -> {
                        List<BucketEntry> sortedList = new ArrayList<>(unsortedList);
                        sortedList.sort(new BucketEntryComparator());
                        return sortedList;
                    })
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onEntriesReceived));
        }

        if (incomeList == null) {
            disposable.add(incomeRepository.getList()
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onIncomesReceived));
        }
    }

    private void onBucketsReceived(final List<Bucket> bucketList) {
        this.bucketList = bucketList;
        if (mainView.get() != null) {
            mainView.get().onBucketListViewReceived(bucketList);
        }
    }

    private void onEntriesReceived(final List<BucketEntry> entries) {
        this.bucketEntryList = entries;
        this.totalSpending = entries.stream().mapToDouble(BucketEntry::getAmount).sum();
        if (mainView.get() != null) {
            mainView.get().onEntriesReceived(entries);
        }
    }

    private void onIncomesReceived(final List<Income> incomeList) {
        this.incomeList = incomeList;
        this.totalIncome = incomeList.stream().mapToDouble(Income::getAmount).sum();
        if (this.totalSpending != null && mainView.get() != null) {
            mainView.get().onIncomeDataReceived(incomeList, (totalIncome - totalSpending));
        }
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    private OnSharedPreferenceChangeListener sharedPreferencesListener() {
        return (sp, key) -> {
            if (key.equals(KEY_PREF_LANGUAGE)) {
                final String languagePref = languagesRepository.getKeyPrefLanguage(key);
                switch (languagePref) {
                    case "en":
                        Locale localeEN = new Locale("en");
                        mainView.get().updateLocale(localeEN);
                        break;
                    case "es":
                        Locale localeES = new Locale("es");
                        mainView.get().updateLocale(localeES);
                        break;

                }
            }
        };
    }

}
