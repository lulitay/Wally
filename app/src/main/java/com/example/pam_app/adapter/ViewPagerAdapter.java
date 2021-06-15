package com.example.pam_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pam_app.fragment.AddIncomeFragment;
import com.example.pam_app.fragment.AddSavingBucketEntryFragment;
import com.example.pam_app.fragment.AddSpendingBucketEntryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    String defaultBucketName;
    int defaultBucketType;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String defaultBucketName, int defaultBucketType) {
        super(fragmentActivity);
        this.defaultBucketName = defaultBucketName;
        this.defaultBucketType = defaultBucketType;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String bucketNamePlaceholder = position == defaultBucketType ? defaultBucketName : null;
        switch(position) {
            case 0:
                return AddSpendingBucketEntryFragment.newInstance(position , bucketNamePlaceholder);
            case 1:
                return AddSavingBucketEntryFragment.newInstance(position, bucketNamePlaceholder);
            case 2:
                return AddIncomeFragment.newInstance(position, bucketNamePlaceholder);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
