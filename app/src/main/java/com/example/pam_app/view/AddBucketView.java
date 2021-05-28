package com.example.pam_app.view;

public interface AddBucketView {

    void onErrorSavingBucket();

    void onSuccessSavingBucket(final String text);

    void goToLoadImage();

    void changeDatePickerState(final boolean state);
}
