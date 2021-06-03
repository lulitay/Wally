package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;

import java.util.List;
import java.util.Locale;

public interface MainView {

    void onBucketListViewReceived(final List<Bucket> bucketList);
    void updateLocale(final Locale locale);
}
