package com.example.pam_app.view;

import com.example.pam_app.model.BucketEntry;

import java.util.List;

public interface HomeView {

    void bind(final List<BucketEntry> entryList);
    void onBucketEntryAdded(final BucketEntry bucketEntry);
}
