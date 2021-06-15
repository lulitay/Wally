package com.example.pam_app.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BucketDao {

    @Query("SELECT * FROM buckets")
    Flowable<List<BucketEntity>> getList();

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM buckets WHERE isRecurrent=:type AND dueDate<:date")
    Flowable<List<BucketEntity>> getList(final boolean type, final Date date);

    @Update
    void update(final BucketEntity bucket);

    @Query("SELECT title FROM buckets WHERE bucketType=:type")
    Flowable<List<String>> getTitleListByType(final int type);

    @Insert
    void create(final BucketEntity bucket);

    @Query("DELETE FROM buckets WHERE id=:id")
    void delete(final int id);

    @Query("SELECT * FROM entries INNER JOIN buckets ON entries.idBucket = buckets.id")
    Flowable<List<BucketEntryWithBucketEntity>> getEntryList();

    @Query("SELECT amount FROM entries JOIN buckets ON idBucket=id WHERE bucketType=:type")
    Flowable<List<Double>> getTotalAmountByType(final int type);

    @Transaction
    @Query("SELECT * FROM buckets WHERE id=:id")
    Flowable<BucketWithEntriesEntity> get(final int id);

    @Transaction
    @Query("SELECT * FROM buckets WHERE title=:title")
    Single<BucketEntity> getBucket(final String title);

    @Insert
    void addEntry(final BucketEntryEntity entry);

    @Delete
    void removeEntry(final BucketEntryEntity entry);
}
