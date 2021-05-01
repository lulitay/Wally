package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.BucketListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BucketListPresenter {
    private final WeakReference<BucketListView> view;
    private final BucketRepository repository;
    private Disposable disposable;

    public BucketListPresenter(final BucketRepository repository, final BucketListView view) {
        this.view = new WeakReference<>(view);
        this.repository = repository;
    }

    public void onViewAttached() {
        disposable = repository.getList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBucketsReceived, this::onBucketsError);
    }

    private void onBucketsReceived(final List<Bucket> model) {
        List<Bucket> spendingBuckets = new ArrayList<>();
        List<Bucket> savingsBuckets = new ArrayList<>();

        for (Bucket bucket : model) {
            if (bucket.bucketType == BucketType.SPENDING) {
                spendingBuckets.add(bucket);
            } else if (bucket.bucketType == BucketType.SAVING) {
                savingsBuckets.add(bucket);
            }
        }

        if (view != null) {
            view.get().bindSpendingBuckets(spendingBuckets);
            view.get().bindSavingsBuckets(savingsBuckets);
        }
    }

    private void onBucketsError(Throwable throwable) {
        //TODO: throw error
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void onBucketClicked(int bucketId) {
        if (view != null) {
            view.get().launchBucketDetailActivity(bucketId);
        }
    }

    public void OnAddBucketClicked() {
        if (view != null) {
            view.get().launchAddBucketActivity();
        }
    }

    public void OnSpendingCardClicked() {
        if (view != null) {
            view.get().collapseSpendingBuckets();
        }
    }

    public void OnSavingsCardClicked() {
        if (view != null) {
            view.get().collapseSavingsBuckets();
        }
    }
}
