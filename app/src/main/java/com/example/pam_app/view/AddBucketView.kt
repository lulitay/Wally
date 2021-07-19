package com.example.pam_app.view

import com.example.pam_app.model.Bucket

interface AddBucketView {
    fun onErrorSavingBucket()
    fun onErrorExistingBucketName()
    fun onSuccessSavingBucket(bucket: Bucket?)
    fun showTitleError(error: Int, parameter: Int?)
    fun showTargetError(error: Int, parameter: Int?)
    fun showDateError(error: Int)
    fun showBucketTypeError(error: Int)
    fun requestStoragePermission()
    fun changeDatePickerState(state: Boolean)
    fun setDefaultValues(bucket: Bucket?)
}