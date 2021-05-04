package com.example.pam_app.presenter;

import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.BucketEntryComparator;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.HomeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class HomePresenter {

    private final WeakReference<HomeView> homeView;
    private final BucketRepository repository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public HomePresenter(
            final BucketRepository repository,
            final HomeView view,
            final SchedulerProvider schedulerProvider
    ) {
        this.repository = repository;
        this.homeView = new WeakReference<>(view);
        this.schedulerProvider = schedulerProvider;
    }

    public void onViewAttached() {
        // TODO check if this is ok
    }

    public void onViewResume() {
        this.disposable = repository.getEntryList()
                .map(unsortedList -> {
                    List<BucketEntry> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new BucketEntryComparator());
                    return sortedList;
                })
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onEntriesComplete);
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    private void onEntriesComplete(final List<BucketEntry> entries) {
        homeView.get().showEntries(entries);
    }
}
