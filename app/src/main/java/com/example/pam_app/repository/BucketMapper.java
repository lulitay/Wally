package com.example.pam_app.repository;

import com.example.pam_app.db.BucketEntity;
import com.example.pam_app.db.BucketEntryEntity;
import com.example.pam_app.db.BucketWithEntriesEntity;
import com.example.pam_app.model.Bucket;
import com.example.pam_app.model.BucketEntry;
import com.example.pam_app.model.BucketType;

import java.util.ArrayList;
import java.util.List;

public class BucketMapper {

    public Bucket toModel(final BucketEntity bucketEntity, final List<BucketEntryEntity> bucketEntryEntityList) {
        List<BucketEntry> entries = null;
        if (bucketEntryEntityList != null) {
            entries = new ArrayList<>();
            for (final BucketEntryEntity bee: bucketEntryEntityList) {
                entries.add(toModel(bee));
            }
        }

        return new Bucket(bucketEntity.title, bucketEntity.dueDate,
                BucketType.values()[bucketEntity.bucketType], bucketEntity.target,
                entries, bucketEntity.id);
    }

    public Bucket toModel(final BucketEntity bucketEntity) {
        return toModel(bucketEntity, null);
    }

    public Bucket toModel(final BucketWithEntriesEntity bucketEntity) {
        return toModel(bucketEntity.bucket, bucketEntity.entries);
    }

    public BucketEntity toEntity(final Bucket bucket) {
        return new BucketEntity(bucket.title, bucket.dueDate, bucket.bucketType.ordinal(),
                bucket.target);
    }

    public BucketEntry toModel(final BucketEntryEntity bucketEntryEntity) {
        return new BucketEntry(bucketEntryEntity.amount, bucketEntryEntity.date,
                bucketEntryEntity.title);
    }

    public BucketEntryEntity toEntity(final BucketEntry bucketEntry, final int idBucket) {
        return new BucketEntryEntity(idBucket, bucketEntry.amount, bucketEntry.date,
                bucketEntry.comment);
    }

}
