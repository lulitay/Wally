package com.example.pam_app.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BucketWithEntriesEntity {
    @Embedded
    public BucketEntity bucket;
    @Relation(parentColumn = "id", entityColumn = "idBucket")
    public List<BucketEntryEntity> entries;
}
