package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.MainView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainPresenter {

    private final WeakReference<MainView> mainView;
    private final BucketRepository repository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public MainPresenter(
            final BucketRepository repository,
            final MainView mainActivity,
            final SchedulerProvider schedulerProvider
    ) {
        this.mainView = new WeakReference<>(mainActivity);
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void onViewAttached() {
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
}
