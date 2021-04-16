package com.example.pam_app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "entries")
@TypeConverters(DateConverter.class)
public class BucketEntryEntity {
    @PrimaryKey(autoGenerate = true)
    public int idEntry;
    public int idBucket;
    public double amount;
    public Date date;
    public String comment;

    public BucketEntryEntity(int idBucket, double amount, Date date, String comment) {
        this.idBucket = idBucket;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }
}
