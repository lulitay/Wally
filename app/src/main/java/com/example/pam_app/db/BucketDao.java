package com.example.pam_app.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface BucketDao {

    @Query("SELECT * FROM buckets")
    Flowable<List<BucketEntity>> getList();

    @Query("SELECT title FROM buckets WHERE bucketType=:type")
    Flowable<List<String>> getTitleListByType(final int type);

    @Insert
    void create(final BucketEntity bucket);

    @Query("DELETE FROM buckets WHERE id=:id")
    void delete(final int id);

    @Transaction
    @Query("SELECT * FROM buckets WHERE id=:id")
    Flowable<BucketWithEntriesEntity> get(final int id);

    @Transaction
    @Query("SELECT * FROM buckets WHERE title=:title")
    Flowable<BucketEntity> getBucket(final String title);

    @Insert
    void addEntry(final BucketEntryEntity entry);

    @Delete
    void removeEntry(final BucketEntryEntity entry);
}
