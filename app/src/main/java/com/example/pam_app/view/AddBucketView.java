package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

public interface AddBucketView {

    void onErrorSavingBucket();

    void onSuccessSavingBucket(final Bucket bucket);

    void goToLoadImage();
}
