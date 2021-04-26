package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

import java.util.List;

public interface BucketListView {
    void bindBuckets(List<Bucket> model);
}
