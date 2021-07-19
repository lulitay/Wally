package com.example.pam_app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pam_app.fragment.AddIncomeFragment
import com.example.pam_app.fragment.AddSavingBucketEntryFragment
import com.example.pam_app.fragment.AddSpendingBucketEntryFragment
import kotlin.jvm.Synchronized

class ViewPagerAdapter(fragmentActivity: FragmentActivity, var defaultBucketName: String, var defaultBucketType: Int) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        val bucketNamePlaceholder = if (position == defaultBucketType) defaultBucketName else null
        return when (position) {
            0 -> AddSpendingBucketEntryFragment.newInstance(position, bucketNamePlaceholder)
            1 -> AddSavingBucketEntryFragment.newInstance(position, bucketNamePlaceholder)
            2 -> AddIncomeFragment.newInstance(position, bucketNamePlaceholder)
            else -> AddSpendingBucketEntryFragment.newInstance(0, bucketNamePlaceholder)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}