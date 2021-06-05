package com.example.pam_app.view;

import com.example.pam_app.model.BucketEntry;

import java.util.List;

public interface AddBucketEntryView {

    int getBucketType();
    void setDropDownOptions(final List<String> buckets);
    void onErrorSavingBucketEntry();
    void onSuccessSavingBucketEntry(final BucketEntry bucketEntry);
}
