package com.example.pam_app.presenter;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.repository.LanguagesRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.MainView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;

import static com.example.pam_app.repository.LanguagesRepositoryImpl.KEY_PREF_LANGUAGE;

public class MainPresenter {

    private final WeakReference<MainView> mainView;
    private final BucketRepository repository;
    private final SchedulerProvider schedulerProvider;
    private final LanguagesRepository languagesRepository;
    private Disposable disposable;

    public MainPresenter(
            final BucketRepository repository,
            final MainView mainActivity,
            final SchedulerProvider schedulerProvider,
            final LanguagesRepository languagesRepository
    ) {
        this.mainView = new WeakReference<>(mainActivity);
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
        this.languagesRepository = languagesRepository;
    }

    public void onViewAttached() {
        languagesRepository.setOnSharedPreferencesListener(sharedPreferencesListener());
        disposable = repository.getList()
            .subscribeOn(schedulerProvider.computation())
            .observeOn(schedulerProvider.ui())
            .subscribe(this::onBucketsReceived, this::onBucketsError);
    }

    private void onBucketsReceived(final List<Bucket> bucketList) {
        mainView.get().onBucketListViewReceived(bucketList);
    }

    private void onBucketsError(Throwable throwable) {
        //TODO: throw error
    }

    public void onViewStop() {
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
