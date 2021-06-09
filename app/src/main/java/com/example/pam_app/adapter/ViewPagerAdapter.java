package com.example.pam_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pam_app.fragment.AddIncomeFragment;
import com.example.pam_app.fragment.AddSavingBucketEntryFragment;
import com.example.pam_app.fragment.AddSpendingBucketEntryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    String defaultBucket;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String defaultBucket) {
        super(fragmentActivity);
        this.defaultBucket = defaultBucket;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return AddSpendingBucketEntryFragment.newInstance(position ,defaultBucket);
            case 1:
                return AddSavingBucketEntryFragment.newInstance(position, defaultBucket);
            case 2:
                return AddIncomeFragment.newInstance(position, defaultBucket);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
