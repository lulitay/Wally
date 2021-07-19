package com.example.pam_app.db

import androidx.room.Embedded
import androidx.room.Relation
import kotlin.jvm.Synchronized

class BucketWithEntriesEntity {
    @kotlin.jvm.JvmField
    @Embedded
    var bucket: BucketEntity? = null

    @kotlin.jvm.JvmField
    @Relation(parentColumn = "id", entityColumn = "idBucket")
    var entries: List<BucketEntryEntity>? = null
}