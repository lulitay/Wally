package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.utils.schedulers.SchedulerProvider;
import com.example.pam_app.view.AddBucketView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class AddBucketPresenter {

    private final WeakReference<AddBucketView> addBucketView;
    private final BucketRepository bucketRepository;
    private final SchedulerProvider schedulerProvider;
    private Disposable disposable;

    public AddBucketPresenter(
            final AddBucketView addBucketView,
            final BucketRepository bucketRepository,
            final SchedulerProvider schedulerProvider
    ) {
        this.addBucketView = new WeakReference<>(addBucketView);
        this.bucketRepository = bucketRepository;
        this.schedulerProvider = schedulerProvider;
    }

    public void saveBucket(final String name, final Date dueDate, final BucketType bucketType,
                           final double target, final String imagePath) {
        final Bucket bucket = new Bucket(name, dueDate, bucketType, target, imagePath);
        disposable = Completable.fromAction(() -> bucketRepository.create(bucket))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    if (addBucketView.get() != null) {
                        addBucketView.get().onSuccessSavingBucket(bucket);
                    }
                }, (throwable) -> {
                    if (addBucketView.get() != null) {
                        addBucketView.get().onErrorSavingBucket();                    }
                });
    }

    public void onClickLoadImage() {
        if (addBucketView.get() != null) {
            addBucketView.get().requestStoragePermission();
        }
    }

    public void onDetachView() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
