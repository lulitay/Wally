package com.example.pam_app.repository;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface BucketRepository {
    Flowable<List<Bucket>> getList();

    void create(final Bucket bucket);

    void delete(final int id);

    Flowable<Bucket> get(final int id);

    Single<Bucket> get(final String title);

    Flowable<List<BucketEntry>> getEntryList();

    Flowable<List<Double>> getSpendingTotal();

    void addEntry(final BucketEntry entry, final int bucketId);

    void removeEntry(final BucketEntry entry, final int idBucket);

    Flowable<List<String>> getTitleListByType(final int type);
}
