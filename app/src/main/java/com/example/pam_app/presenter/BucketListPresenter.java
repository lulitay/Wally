package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.view.BucketListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BucketListPresenter {
    private final WeakReference<BucketListView> view;

    public BucketListPresenter(final BucketListView view) {
        this.view = new WeakReference<>(view);
    }

    public void onBucketsReceived(final List<Bucket> model) {
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

    public void onBucketClicked(int bucketId) {
        if (view.get() != null) {
            view.get().launchBucketDetailActivity(bucketId);
        }
    }

    public void onAddBucketClicked() {
        if (view.get() != null) {
            view.get().launchAddBucketActivity();
        }
    }

    public void onSpendingCardClicked() {
        if (view.get() != null) {
            view.get().collapseSpendingBuckets();
        }
    }

    public void onSavingsCardClicked() {
        if (view.get() != null) {
            view.get().collapseSavingsBuckets();
        }
    }
}
