package com.example.pam_app.presenter

import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketType
import com.example.pam_app.view.BucketListView
import java.lang.ref.WeakReference
import java.util.*
import kotlin.jvm.Synchronized

class BucketListPresenter(view: BucketListView?) {
    private val view: WeakReference<BucketListView?>
    fun onBucketsReceived(model: List<Bucket?>?) {
        val spendingBuckets: MutableList<Bucket?> = ArrayList()
        val savingsBuckets: MutableList<Bucket?> = ArrayList()
        for (bucket in model!!) {
            if (bucket!!.bucketType == BucketType.SPENDING) {
                spendingBuckets.add(bucket)
            } else if (bucket.bucketType == BucketType.SAVING) {
                savingsBuckets.add(bucket)
            }
        }
        if (view.get() != null) {
            view.get()!!.bindSpendingBuckets(spendingBuckets)
            view.get()!!.bindSavingsBuckets(savingsBuckets)
            view.get()!!.setIsSpendingListEmpty(spendingBuckets.isEmpty())
            view.get()!!.setIsSavingsListEmpty(savingsBuckets.isEmpty())
            view.get()!!.drawSpendingBucketList()
            view.get()!!.drawSavingsBucketList()
        }
    }

    fun onBucketClicked(bucketId: Int) {
        if (view.get() != null) {
            view.get()!!.launchBucketDetailActivity(bucketId)
        }
    }

    fun onAddBucketClicked() {
        if (view.get() != null) {
            view.get()!!.launchAddBucketActivity()
        }
    }

    fun onSpendingCardClicked() {
        if (view.get() != null) {
            view.get()!!.collapseSpendingBuckets()
        }
    }

    fun onSavingsCardClicked() {
        if (view.get() != null) {
            view.get()!!.collapseSavingsBuckets()
        }
    }

    init {
        this.view = WeakReference(view)
    }
}