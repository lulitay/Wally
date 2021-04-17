package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketType;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.AddBucketView;

import java.lang.ref.WeakReference;
import java.util.Date;

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
        bucketRepository.create(bucket);
    }
}
