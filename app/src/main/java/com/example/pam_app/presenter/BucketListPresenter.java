package com.example.pam_app.presenter;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.repository.BucketRepository;
import com.example.pam_app.view.BucketListView;

import java.lang.ref.WeakReference;
import java.util.List;

public class BucketListPresenter {
    private final WeakReference<BucketListView> bucketListView;
    private final BucketRepository bucketRepository;

    public BucketListPresenter(final BucketListView bucketListView, final BucketRepository bucketRepository) {
        this.bucketListView = new WeakReference<>(bucketListView);
        this.bucketRepository = bucketRepository;
    }

    public List<Bucket> getBuckets() {
        return bucketRepository.getList().blockingFirst();
    }
}
