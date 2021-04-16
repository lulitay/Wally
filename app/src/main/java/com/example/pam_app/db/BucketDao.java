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

    @Insert
    void create(final BucketEntity bucket);

    @Delete
    void delete(final BucketEntity bucketEntity);

    @Transaction
    @Query("SELECT * FROM buckets WHERE id=:id")
    BucketWithEntriesEntity get(final int id); //TODO check async call??????

    @Insert
    void addEntry(final BucketEntryEntity entry);

    @Delete
    void removeEntry(final BucketEntryEntity entry);
}
