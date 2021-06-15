package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.utils.listener.Clickable;

import java.io.Serializable;
import java.util.List;

public interface BucketView {
    void bind(Bucket bucket);
    void goToAddEntry(String bucketName, int bucketType);
    void back(Serializable entries);
    void showGetBucketError();
    void showDeleteBucketError();
    void showDeleteBucketSuccess();
    void showSureDialog(Clickable clickable);
}
