package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.utils.listener.Clickable;

public interface BucketView {
    void bind(Bucket bucket);
    void back();
    void goToAddEntry(String bucketName, int bucketType);
    void showGetBucketError();
    void showDeleteBucketError();
    void showDeleteBucketSuccess();
    void showSureDialog(Clickable clickable);
}
