package com.example.pam_app.view;

import com.example.pam_app.model.BucketEntry;

import java.util.List;

public interface AddBucketEntryView {

    int getBucketType();
    void setDropDownOptions(final List<String> buckets);
    void onErrorSavingBucketEntry();
    void onSuccessSavingBucketEntry(final BucketEntry bucketEntry);

    void showDescriptionError(int error, Integer parameter);

    void showAmountError(int error, Integer parameter);

    void showDateError(int error);

    void showBucketTitleError(int error);
}
