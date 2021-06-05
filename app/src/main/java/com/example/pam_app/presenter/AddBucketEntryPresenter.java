package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketEntryView;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class AddBucketEntryPresenter {

    private final WeakReference<AddBucketEntryView> addBucketEntryView;
    private final BucketRepository bucketRepository;
    private final SchedulerProvider schedulerProvider;
    private final CompositeDisposable disposable;

    public AddBucketEntryPresenter(
            final AddBucketEntryView addBucketEntryView,
            final BucketRepository bucketRepository,
            final SchedulerProvider schedulerProvider) {
        this.addBucketEntryView = new WeakReference<>(addBucketEntryView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
        this.disposable = new CompositeDisposable();
    }

    public void onViewAttached() {
        if (addBucketEntryView.get() != null) {
            final List<String> buckets = bucketRepository.getTitleListByType(
                    addBucketEntryView.get().getBucketType()
            ).blockingFirst();
            addBucketEntryView.get().setDropDownOptions(buckets);
        }
    }

    public void onViewDetached() {
        disposable.dispose();
    }

    public void saveBucketEntry(final double amount, final Date date, final String description,
                                final String bucketTitle) {
        final BucketEntry entry = new BucketEntry(amount, date, description);
        disposable.add(
                bucketRepository.get(bucketTitle)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((Bucket bucket) -> disposable.add(
                        Completable.fromAction(() ->  bucketRepository.addEntry(entry, bucket.id))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> {
                            if (addBucketEntryView.get() != null) {
                                addBucketEntryView.get().onSuccessSavingBucketEntry(entry);
                            } }, (throwable) -> {
                            if (addBucketEntryView.get() != null) {
                                addBucketEntryView.get().onErrorSavingBucketEntry();
                            }
                        })
                ), (throwable) -> {
                    if (addBucketEntryView.get() != null) {
                        addBucketEntryView.get().onErrorSavingBucketEntry();
                    }
                })
        );
    }
}
