package com.example.pam_app.repository

import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import kotlin.jvm.Synchronized

interface BucketRepository {
    val list: Flowable<List<Bucket>>
    fun getList(type: Boolean, date: Date?): Flowable<List<Bucket?>>
    fun update(bucket: Bucket?): Single<Int?>
    fun create(bucket: Bucket?): Single<Long?>
    fun delete(id: Int): Single<Int?>
    operator fun get(id: Int): Flowable<Bucket>
    operator fun get(title: String?): Single<Bucket>
    val entryList: Flowable<List<BucketEntry>>
    val spendingTotal: Flowable<List<Double?>?>?
    fun addEntry(entry: BucketEntry, bucketId: Int): Single<Long?>
    fun removeEntry(entry: BucketEntry, idBucket: Int)
    fun getTitleListByType(type: Int): Flowable<List<String?>?>?
}