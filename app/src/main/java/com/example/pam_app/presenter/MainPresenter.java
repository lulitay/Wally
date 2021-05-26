package com.example.pam_app.presenter;

import com.example.pam_app.MainActivity;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainPresenter {

    private final WeakReference<MainActivity> mainActivity;
    private final BucketRepository repository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public MainPresenter(
            final BucketRepository repository,
            final MainActivity mainActivity,
            final SchedulerProvider schedulerProvider
    ) {
        this.mainActivity = new WeakReference<>(mainActivity);
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void getBucketListViewBuckets() {
        disposable = repository.getList()
            .subscribeOn(schedulerProvider.computation())
            .observeOn(schedulerProvider.ui())
            .subscribe(this::onBucketsReceived, this::onBucketsError);
    }

    private void onBucketsReceived(final List<Bucket> bucketList) {
        mainActivity.get().onBucketListViewReceived(bucketList);
    }

    private void onBucketsError(Throwable throwable) {
        //TODO: throw error
    }

    public void onViewStop() {
        disposable.dispose();
    }

    public void onViewResume() {
        //disposable
    }
}
