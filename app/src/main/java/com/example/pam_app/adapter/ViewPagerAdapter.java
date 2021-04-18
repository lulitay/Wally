package com.example.pam_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pam_app.fragment.AddIncomeBucketEntryFragment;
import com.example.pam_app.fragment.AddSavingBucketEntryFragment;
import com.example.pam_app.fragment.AddSpendingBucketEntryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 1:
                return AddSavingBucketEntryFragment.newInstance(position);
            case 2:
                return AddIncomeBucketEntryFragment.newInstance(position);
            default:
                return AddSpendingBucketEntryFragment.newInstance(position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
