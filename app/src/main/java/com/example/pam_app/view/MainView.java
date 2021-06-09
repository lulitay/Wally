package com.example.pam_app.view;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.Income;

import java.util.List;
import java.util.Locale;

public interface MainView {

    void onBucketListViewReceived(final List<Bucket> bucketList);
    void onEntriesReceived(final List<BucketEntry> entryList);
    void onIncomeDataReceived(final List<Income> incomeList, final Double incomeLeft);
    void updateLocale(final Locale locale);
}
