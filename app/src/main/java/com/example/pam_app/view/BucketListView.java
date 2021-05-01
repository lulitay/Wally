package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

import java.util.List;

public interface BucketListView {
    void bindSpendingBuckets(List<Bucket> model);

    void bindSavingsBuckets(List<Bucket> model);

    void launchBucketDetailActivity(int position);

    void launchAddBucketActivity();

    void collapseSpendingBuckets();

    void collapseSavingsBuckets();
}
