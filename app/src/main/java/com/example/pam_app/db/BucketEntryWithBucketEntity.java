package com.example.pam_app.db;

import androidx.room.Embedded;

public class BucketEntryWithBucketEntity {
    @Embedded
    public BucketEntryEntity bucketEntryEntity;

    @Embedded
    public BucketEntity bucketEntity;
}
