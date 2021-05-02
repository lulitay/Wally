package com.example.pam_app.presenter;

import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.BucketEntryComparator;
import com.example.pam_app.view.HomeView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    private final WeakReference<HomeView> homeView;
    private final BucketRepository repository;
    private Disposable disposable;

    public HomePresenter(final BucketRepository repository, final HomeView view) {
        this.homeView = new WeakReference<>(view);
        this.repository = repository;
    }

    public void onViewAttach() {
        this.disposable = repository.getEntryList()
                .map(unsortedList -> {
                    List<BucketEntry> sortedList = new ArrayList<>(unsortedList);
                    Collections.sort(sortedList, new BucketEntryComparator());
                    return sortedList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onEntriesComplete);
    }

    /*public void onViewResume() {
        disposable.add(
                repository.getEntryList()
                        .map(unsortedList -> {
                            List<BucketEntry> sortedList = new ArrayList<>(unsortedList);
                            Collections.sort(sortedList, new BucketEntryComparator());
                            return sortedList;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(this::onEntriesComplete)
        );
    }

    public void onViewPause() {
        disposable.clear();
    }*/

    public void onViewDetached() {
        disposable.dispose();
    }

    private void onEntriesComplete(final List<BucketEntry> entries) {
        if (homeView != null) {
            homeView.get().showEntries(entries);
        }
    }
}
