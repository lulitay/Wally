package com.example.pam_app.view

import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.model.Income
import java.util.*
import kotlin.jvm.Synchronized

interface MainView {
    fun onBucketListViewReceived(bucketList: List<Bucket>?)
    fun onEntriesReceived(entryList: MutableList<BucketEntry>?)
    fun onIncomeDataReceived(incomeList: List<Income>?, incomeLeft: Double)
    fun updateLocale(locale: Locale?)
    fun onDeleteBucket(id: Int)
    fun onUpdateBucket(bucket: Bucket)
}