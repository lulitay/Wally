package com.example.pam_app.entry;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 1:
                return AddSavingEntryFragment.newInstance(position);
            case 2:
                return AddIncomeEntryFragment.newInstance(position);
            default:
                return AddSpendingEntryFragment.newInstance(position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
