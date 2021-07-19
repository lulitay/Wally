package com.example.pam_app.repository

import com.example.pam_app.db.BucketEntity
import com.example.pam_app.db.BucketEntryEntity
import com.example.pam_app.db.BucketEntryWithBucketEntity
import com.example.pam_app.db.BucketWithEntriesEntity
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.model.BucketType
import java.util.*

class BucketMapper {
    @kotlin.jvm.JvmOverloads
    fun toModel(bucketEntity: BucketEntity?, bucketEntryEntityList: List<BucketEntryEntity?>? = null): Bucket {
        var entries: MutableList<BucketEntry?>? = null
        if (bucketEntryEntityList != null) {
            entries = ArrayList()
            for (bee in bucketEntryEntityList) {
                entries.add(toModel(bee))
            }
        }
        return Bucket(bucketEntity!!.title, bucketEntity.dueDate,
                BucketType.values()[bucketEntity.bucketType], bucketEntity.target,
                entries, bucketEntity.id, bucketEntity.imagePath, bucketEntity.isRecurrent)
    }

    fun toModel(bucketEntity: BucketWithEntriesEntity?): Bucket {
        return toModel(bucketEntity!!.bucket, bucketEntity.entries)
    }

    fun toEntity(bucket: Bucket?): BucketEntity {
        return BucketEntity(bucket!!.title, bucket.dueDate, bucket.bucketType.ordinal,
                bucket.target, bucket.imagePath, bucket.isRecurrent, bucket.id)
    }

    fun toModel(bucketEntryEntity: BucketEntryEntity?): BucketEntry {
        return BucketEntry(bucketEntryEntity!!.amount, bucketEntryEntity.date,
                bucketEntryEntity.comment, bucketEntryEntity.idBucket)
    }

    fun toModel(bucketEntryEntity: BucketEntryWithBucketEntity?): BucketEntry {
        return BucketEntry(bucketEntryEntity!!.bucketEntryEntity!!.amount, bucketEntryEntity.bucketEntryEntity!!.date,
                bucketEntryEntity.bucketEntryEntity!!.comment, bucketEntryEntity.bucketEntity!!.title, bucketEntryEntity.bucketEntity!!.id)
    }

    fun toEntity(bucketEntry: BucketEntry, idBucket: Int): BucketEntryEntity {
        return BucketEntryEntity(idBucket, bucketEntry.amount, bucketEntry.date,
                bucketEntry.comment)
    }
}