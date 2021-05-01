package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

public interface BucketView {
    void bind(Bucket bucket);
    void back();
    void goToAddEntry();
}
