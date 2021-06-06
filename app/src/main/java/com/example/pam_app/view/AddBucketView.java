package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

public interface AddBucketView {

    void onErrorSavingBucket();
    void onSuccessSavingBucket(final Bucket bucket);
    void goToLoadImage();
    void showTitleError(final int error, final Integer parameter);
    void showTargetError(final int error, final Integer parameter);
    void showDateError(final int error);
    void showBucketTypeError(final int error);
}
