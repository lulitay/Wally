package com.example.pam_app.view

import com.example.pam_app.model.BucketEntry
import kotlin.jvm.Synchronized

interface HomeView {
    fun bind(entryList: List<BucketEntry>?)
    fun onBucketEntryAdded(bucketEntry: BucketEntry?)
}