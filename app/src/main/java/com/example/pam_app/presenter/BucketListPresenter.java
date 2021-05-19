package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.BucketListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BucketListPresenter {
    private final WeakReference<BucketListView> view;
    private final BucketRepository repository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public BucketListPresenter(final BucketRepository repository, final BucketListView view, final SchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void onViewAttached() {
        disposable = repository.getList()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onBucketsReceived, this::onBucketsError);
    }

    private void onBucketsReceived(final List<Bucket> model) {
        final List<Bucket> spendingBuckets = new ArrayList<>();
        final List<Bucket> savingsBuckets = new ArrayList<>();

        for (Bucket bucket : model) {
            if (bucket.bucketType == BucketType.SPENDING) {
                spendingBuckets.add(bucket);
            } else if (bucket.bucketType == BucketType.SAVING) {
                savingsBuckets.add(bucket);
            }
        }

        if (view.get() != null) {
            view.get().bindSpendingBuckets(spendingBuckets);
            view.get().bindSavingsBuckets(savingsBuckets);
            view.get().setIsSpendingListEmpty(spendingBuckets.isEmpty());
            view.get().setIsSavingsListEmpty(savingsBuckets.isEmpty());
            view.get().drawSpendingBucketList();
            view.get().drawSavingsBucketList();
        }
    }

    private void onBucketsError(Throwable throwable) {
        //TODO: throw error
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void onBucketClicked(int bucketId) {
        if (view.get() != null) {
            view.get().launchBucketDetailActivity(bucketId);
        }
    }

    public void OnAddBucketClicked() {
        if (view.get() != null) {
            view.get().launchAddBucketActivity();
        }
    }

    public void OnSpendingCardClicked() {
        if (view.get() != null) {
            view.get().collapseSpendingBuckets();
        }
    }

    public void OnSavingsCardClicked() {
        if (view.get() != null) {
            view.get().collapseSavingsBuckets();
        }
    }
}
