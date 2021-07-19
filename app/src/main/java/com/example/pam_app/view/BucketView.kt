package com.example.pam_app.view

import com.example.pam_app.model.Bucket
import java.io.Serializable
import java.util.*

interface BucketView {
    fun bind(bucket: Bucket?)
    fun goToAddEntry(bucketName: String?, bucketType: Int)
    fun back(entries: Serializable?, id: Int?)
    fun showGetBucketError()
    fun showDeleteBucketError()
    fun showDeleteBucketSuccess()
    fun showSureDialog(clickable: () -> Unit)
    fun goToAddBucket(bucketId: Int)
}