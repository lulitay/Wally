package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.AddBucketView;

import java.lang.ref.WeakReference;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddBucketPresenter {

    private final WeakReference<AddBucketView> addBucketView;
    private final BucketRepository bucketRepository;

    public AddBucketPresenter(final AddBucketView addBucketView, final BucketRepository bucketRepository) {
        this.addBucketView = new WeakReference<>(addBucketView);
        this.bucketRepository = bucketRepository;
    }

    public void saveBucket(final String name, final Date dueDate, final BucketType bucketType,
                           final double target) {
        final Bucket bucket = new Bucket(name, dueDate, bucketType, target);
        Completable.fromAction(() -> bucketRepository.create(bucket))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError(e -> addBucketView.get().onErrorSavingBucket())
                .doOnComplete(() -> addBucketView.get().onSuccessSavingBucket(name))
                .subscribe();
    }
}
