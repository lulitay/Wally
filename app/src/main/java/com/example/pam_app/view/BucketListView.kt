package com.example.pam_app.view

import android.content.Context
import com.example.pam_app.model.Bucket
import com.example.pam_app.utils.listener.ClickableTarget
import kotlin.jvm.Synchronized

interface BucketListView {
    fun bindSpendingBuckets(model: List<Bucket?>?)
    fun bindSavingsBuckets(model: List<Bucket?>?)
    fun launchBucketDetailActivity(position: Int)
    fun launchAddBucketActivity()
    fun collapseSpendingBuckets()
    fun collapseSavingsBuckets()
    fun bind(context: Context, launchAddBucketActivity: () -> Unit,
             launchBucketDetailActivity: (Int) -> Unit, bucketList: List<Bucket?>?)

    fun setIsSpendingListEmpty(isEmpty: Boolean)
    fun setIsSavingsListEmpty(isEmpty: Boolean)
    fun onBucketAdded(bucket: Bucket?)
    fun drawSpendingBucketList()
    fun drawSavingsBucketList()
    fun onDeleteBucket(id: Int)
}