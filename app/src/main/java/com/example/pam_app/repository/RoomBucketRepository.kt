package com.example.pam_app.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.pam_app.db.BucketDao
import com.example.pam_app.db.BucketEntity
import com.example.pam_app.db.BucketEntryWithBucketEntity
import com.example.pam_app.db.BucketWithEntriesEntity
import com.example.pam_app.model.Bucket
import com.example.pam_app.model.BucketEntry
import com.example.pam_app.model.BucketType
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import kotlin.jvm.Synchronized

class RoomBucketRepository(private val bucketDao: BucketDao?, private val bucketMapper: BucketMapper?) : BucketRepository {
    override val list: Flowable<List<Bucket>>
        get() = bucketDao!!.list.map { bucketEntityList: List<BucketEntity?>? ->
            val buckets: MutableList<Bucket> = ArrayList()
            for (be in bucketEntityList!!) {
                buckets.add(bucketMapper!!.toModel(be))
            }
            buckets
        }

    override fun getList(type: Boolean, date: Date?): Flowable<List<Bucket?>> {
        return bucketDao!!.getList(type, date).map { bucketEntityList: List<BucketEntity?>? ->
            val buckets: MutableList<Bucket?> = ArrayList()
            for (be in bucketEntityList!!) {
                buckets.add(bucketMapper!!.toModel(be))
            }
            buckets
        }
    }

    override fun update(bucket: Bucket?): Single<Int?> {
        return Single.fromCallable { bucketDao!!.update(bucketMapper!!.toEntity(bucket))}
    }

    override fun create(bucket: Bucket?): Single<Long?> {
        return Single.fromCallable {
            try {
                return@fromCallable bucketDao!!.create(bucketMapper!!.toEntity(bucket))
            } catch (e: SQLiteConstraintException) {
                return@fromCallable -1L
            }
        }
    }

    override fun delete(id: Int): Single<Int?> {
        return Single.fromCallable { bucketDao!!.delete(id) }
    }

    override fun get(id: Int): Single<Bucket> {
        return bucketDao!![id].map { bucketEntity: BucketWithEntriesEntity? -> bucketMapper!!.toModel(bucketEntity) }
    }

    override fun get(title: String?): Single<Bucket> {
        return bucketDao!!.getBucket(title).map { bucketEntity: BucketEntity? -> bucketMapper!!.toModel(bucketEntity) }
    }

    override val entryList: Flowable<List<BucketEntry>>
        get() = bucketDao?.entryList?.map { bucketEntryEntityList: List<BucketEntryWithBucketEntity?>? ->
            val entries: MutableList<BucketEntry> = ArrayList()
            for (be in bucketEntryEntityList!!) {
                entries.add(bucketMapper!!.toModel(be))
            }
            entries
        }!!

    override val spendingTotal: Flowable<List<Double?>?>?
        get() = bucketDao!!.getTotalAmountByType(BucketType.SPENDING.ordinal)

    override fun addEntry(entry: BucketEntry, bucketId: Int): Single<Long?> {
        return Single.fromCallable { bucketDao!!.addEntry(bucketMapper!!.toEntity(entry, bucketId)) }
    }

    override fun removeEntry(entry: BucketEntry, idBucket: Int) {
        bucketDao!!.removeEntry(bucketMapper!!.toEntity(entry, idBucket))
    }

    override fun getTitleListByType(type: Int): Flowable<List<String?>?>? {
        return bucketDao!!.getTitleListByType(type)
    }

}