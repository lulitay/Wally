package com.example.pam_app.view;

import android.content.Context;

import com.example.pam_app.utils.listener.Clickable;
import com.example.pam_app.utils.listener.ClickableWithParameter;
import com.example.pam_app.model.Bucket;

import java.util.List;

public interface BucketListView {
    void bindSpendingBuckets(List<Bucket> model);

    void bindSavingsBuckets(List<Bucket> model);

    void launchBucketDetailActivity(int position);

    void launchAddBucketActivity();

    void collapseSpendingBuckets();

    void collapseSavingsBuckets();

    void bind(Context context, Clickable launchAddBucketActivity,
              ClickableWithParameter<Integer> launchBucketDetailActivity, final List<Bucket> bucketList);

    void setIsSpendingListEmpty(boolean isEmpty);

    void setIsSavingsListEmpty(boolean isEmpty);

    void onBucketAdded(final Bucket bucket);

    void drawSpendingBucketList();

    void drawSavingsBucketList();
}
