package com.example.pam_app.repository;

import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;

import java.util.List;

import io.reactivex.Flowable;

public interface BucketRepository {
    Flowable<List<Bucket>> getList();

    void create(final Bucket bucket);

    void delete(final Bucket bucket);

    Flowable<Bucket> get(final int id);

    Flowable<Bucket> get(final String title);

    void addEntry(final BucketEntry entry, final String bucketTitle);

    void removeEntry(final BucketEntry entry, final int idBucket);

    Flowable<List<String>> getTitleListByType(final int type);
}
