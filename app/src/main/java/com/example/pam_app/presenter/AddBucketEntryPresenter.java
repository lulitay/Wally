package com.example.pam_app.presenter;

import com.example.pam_app.view.AddBucketEntryView;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.repository.BucketRepository;

import java.lang.ref.WeakReference;
import java.util.Date;

public class AddBucketEntryPresenter {

    private final WeakReference<AddBucketEntryView> addBucketEntryView;
    private final BucketRepository bucketRepository;

    public AddBucketEntryPresenter(final AddBucketEntryView addBucketEntryView, final BucketRepository bucketRepository) {
        this.addBucketEntryView = new WeakReference<>(addBucketEntryView);
        this.bucketRepository = bucketRepository;
    }

    public void onViewAttached() {

    }

    public void onViewDetached() {

    }

    public void saveBucketEntry(final double amount, final Date date, final String description) {
        final BucketEntry entry = new BucketEntry(amount, date, description);
        bucketRepository.addEntry(entry, 0); //TODO change this
    }
}
