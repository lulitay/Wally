package com.example.pam_app.db

import androidx.room.Embedded
import kotlin.jvm.Synchronized

class BucketEntryWithBucketEntity {
    @kotlin.jvm.JvmField
    @Embedded
    var bucketEntryEntity: BucketEntryEntity? = null

    @kotlin.jvm.JvmField
    @Embedded
    var bucketEntity: BucketEntity? = null
}