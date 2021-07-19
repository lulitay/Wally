package com.example.pam_app.view

import com.example.pam_app.model.BucketEntry
import kotlin.jvm.Synchronized

interface AddBucketEntryView {
    val bucketType: Int
    fun setDropDownOptions(buckets: List<String?>?)
    fun onErrorSavingBucketEntry()
    fun onSuccessSavingBucketEntry(bucketEntry: BucketEntry)
    fun showDescriptionError(error: Int, parameter: Int?)
    fun showAmountError(error: Int, parameter: Int?)
    fun showDateError(error: Int)
    fun showBucketTitleError(error: Int)
}