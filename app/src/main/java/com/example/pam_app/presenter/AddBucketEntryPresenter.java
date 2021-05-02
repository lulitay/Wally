package com.example.pam_app.presenter;

import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.AddBucketEntryView;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddBucketEntryPresenter {

    private final WeakReference<AddBucketEntryView> addBucketEntryView;
    private final BucketRepository bucketRepository;

    public AddBucketEntryPresenter(final AddBucketEntryView addBucketEntryView, final BucketRepository bucketRepository) {
        this.addBucketEntryView = new WeakReference<>(addBucketEntryView);
        this.bucketRepository = bucketRepository;
    }

    public void onViewAttached() {
        if (addBucketEntryView.get() != null) {
            final List<String> buckets = bucketRepository.getTitleListByType(
                    addBucketEntryView.get().getBucketType()
            ).blockingFirst();
            addBucketEntryView.get().setDropDownOptions(buckets);
        }
    }

    public void saveBucketEntry(final double amount, final Date date, final String description,
                                final String bucketTitle) {
        final BucketEntry entry = new BucketEntry(amount, date, description);
        Completable.fromAction(() -> bucketRepository.addEntry(entry, bucketTitle))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError(e -> addBucketEntryView.get().onErrorSavingBucketEntry())
                .doOnComplete(() -> addBucketEntryView.get().onSuccessSavingBucketEntry(description))
                .subscribe();
    }
}
