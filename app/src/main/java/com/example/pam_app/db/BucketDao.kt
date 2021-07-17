package com.example.pam_app.db

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import kotlin.jvm.Synchronized

@Dao
interface BucketDao {
    @get:Query("SELECT * FROM buckets")
    val list: Flowable<List<BucketEntity?>?>

    @TypeConverters(DateConverter::class)
    @Query("SELECT * FROM buckets WHERE isRecurrent=:type AND dueDate<:date")
    fun getList(type: Boolean, date: Date?): Flowable<List<BucketEntity?>?>

    @Update
    fun update(bucket: BucketEntity?): Int

    @Query("SELECT title FROM buckets WHERE bucketType=:type")
    fun getTitleListByType(type: Int): Flowable<List<String?>?>?

    @Insert
    fun create(bucket: BucketEntity?): Long

    @Query("DELETE FROM buckets WHERE id=:id")
    fun delete(id: Int): Int

    @get:Query("SELECT * FROM entries INNER JOIN buckets ON entries.idBucket = buckets.id")
    val entryList: Flowable<List<BucketEntryWithBucketEntity?>?>

    @Query("SELECT amount FROM entries JOIN buckets ON idBucket=id WHERE bucketType=:type")
    fun getTotalAmountByType(type: Int): Flowable<List<Double?>?>?

    @Transaction
    @Query("SELECT * FROM buckets WHERE id=:id")
    operator fun get(id: Int): Flowable<BucketWithEntriesEntity?>

    @Transaction
    @Query("SELECT * FROM buckets WHERE title=:title")
    fun getBucket(title: String?): Single<BucketEntity?>

    @Insert
    fun addEntry(entry: BucketEntryEntity?): Long

    @Delete
    fun removeEntry(entry: BucketEntryEntity?)
}